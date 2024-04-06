/*
 * Note
 * This code created based on Swipeable.kt in androidx.compose.material:material:1.4.3.
 *
 */
package io.groovin.expandablebox

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.groovin.expandablebox.ExpandableBoxSwipeableDefaults.AnimationSpec
import io.groovin.expandablebox.ExpandableBoxSwipeableDefaults.StandardResistanceFactor
import io.groovin.expandablebox.ExpandableBoxSwipeableDefaults.VelocityThreshold
import io.groovin.expandablebox.ExpandableBoxSwipeableDefaults.resistanceConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sin

/**
 * State of the [ExpandableBox] Composable.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Stable
open class ExpandableBoxState(
    initialValue: ExpandableBoxStateValue,
    internal val animationSpec: AnimationSpec<Float> = AnimationSpec,
    internal val confirmStateChange: (newValue: ExpandableBoxStateValue) -> Boolean = { true }
) {

    var completedValue: ExpandableBoxStateValue by mutableStateOf(initialValue)
        private set

    var progressValue: ExpandableBoxStateValue by mutableStateOf(initialValue)
        internal set

    var isAnimationRunning: Boolean by mutableStateOf(false)
        private set

    val offset: State<Float> get() = offsetState
    val overflow: State<Float> get() = overflowState

    private val offsetState = mutableFloatStateOf(0f)
    private val overflowState = mutableFloatStateOf(0f)
    private val absoluteOffset = mutableFloatStateOf(0f)
    private val animationTarget = mutableStateOf<Float?>(null)

    internal var anchors by mutableStateOf(emptyMap<Float, ExpandableBoxStateValue>())

    private val latestNonEmptyAnchorsFlow: Flow<Map<Float, ExpandableBoxStateValue>> =
        snapshotFlow { anchors }
            .filter { it.isNotEmpty() }
            .take(1)

    private var minBound = Float.NEGATIVE_INFINITY
    private var maxBound = Float.POSITIVE_INFINITY

    internal fun ensureInit(newAnchors: Map<Float, ExpandableBoxStateValue>) {
        if (anchors.isEmpty()) {
            // need to do initial synchronization synchronously :(
            val initialOffset = newAnchors.getOffset(completedValue)
            requireNotNull(initialOffset) {
                "The initial value must have an associated anchor."
            }
            offsetState.floatValue = initialOffset
            absoluteOffset.floatValue = initialOffset
        }
    }

    internal suspend fun processNewAnchors(
        oldAnchors: Map<Float, ExpandableBoxStateValue>,
        newAnchors: Map<Float, ExpandableBoxStateValue>
    ) {
        if (oldAnchors.isEmpty()) {
            // If this is the first time that we receive anchors, then we need to initialise
            // the state so we snap to the offset associated to the initial value.
            minBound = newAnchors.keys.minOrNull()!!
            maxBound = newAnchors.keys.maxOrNull()!!
            val initialOffset = newAnchors.getOffset(completedValue)
            requireNotNull(initialOffset) {
                "The initial value must have an associated anchor."
            }
            snapInternalToOffset(initialOffset)
        } else if (newAnchors != oldAnchors) {
            // If we have received new anchors, then the offset of the current value might
            // have changed, so we need to animate to the new offset. If the current value
            // has been removed from the anchors then we animate to the closest anchor
            // instead. Note that this stops any ongoing animation.
            minBound = Float.NEGATIVE_INFINITY
            maxBound = Float.POSITIVE_INFINITY
            val animationTargetValue = animationTarget.value
            // if we're in the animation already, let's find it a new home
            val targetOffset = if (animationTargetValue != null) {
                // first, try to map old state to the new state
                val oldState = oldAnchors[animationTargetValue]
                val newState = newAnchors.getOffset(oldState)
                // return new state if exists, or find the closes one among new anchors
                newState ?: newAnchors.keys.minByOrNull { abs(it - animationTargetValue) }!!
            } else {
                // we're not animating, proceed by finding the new anchors for an old value
                val actualOldValue = oldAnchors[offset.value]
                val value = if (actualOldValue == completedValue) completedValue else actualOldValue
                newAnchors.getOffset(value) ?: newAnchors
                    .keys.minByOrNull { abs(it - offset.value) }!!
            }
            try {
                animateInternalToOffset(targetOffset, animationSpec)
            } catch (c: CancellationException) {
                // If the animation was interrupted for any reason, snap as a last resort.
                snapInternalToOffset(targetOffset)
            } finally {
                completedValue = newAnchors.getValue(targetOffset)
                minBound = newAnchors.keys.minOrNull()!!
                maxBound = newAnchors.keys.maxOrNull()!!
            }
        }
    }

    internal var thresholds: (Float, Float) -> Float by mutableStateOf({ _, _ -> 0f })

    internal var velocityThreshold by mutableFloatStateOf(0f)

    internal var resistance: ResistanceConfig? by mutableStateOf(null)

    internal val draggableState = DraggableState {
        val newAbsolute = absoluteOffset.floatValue + it
        val clamped = newAbsolute.coerceIn(minBound, maxBound)
        val overflow = newAbsolute - clamped
        val resistanceDelta = resistance?.computeResistance(overflow) ?: 0f
        offsetState.floatValue = clamped + resistanceDelta
        overflowState.floatValue = overflow
        absoluteOffset.floatValue = newAbsolute
    }

    private suspend fun snapInternalToOffset(target: Float) {
        draggableState.drag {
            dragBy(target - absoluteOffset.floatValue)
        }
    }

    private suspend fun animateInternalToOffset(target: Float, spec: AnimationSpec<Float>) {
        draggableState.drag {
            var prevValue = absoluteOffset.floatValue
            animationTarget.value = target
            isAnimationRunning = true
            try {
                Animatable(prevValue).animateTo(target, spec) {
                    dragBy(this.value - prevValue)
                    prevValue = this.value
                }
            } finally {
                animationTarget.value = null
                isAnimationRunning = false
            }
        }
    }

    val targetValue: ExpandableBoxStateValue
        get() {
            // TODO(calintat): Track current velocity (b/149549482) and use that here.
            val target = animationTarget.value ?: computeTarget(
                offset = offset.value,
                lastValue = anchors.getOffset(completedValue) ?: offset.value,
                anchors = anchors.keys,
                thresholds = thresholds,
                velocity = 0f,
                velocityThreshold = Float.POSITIVE_INFINITY
            )
            return anchors[target] ?: completedValue
        }

    val progress: ExpandableBoxSwipeProgress
        get() {
            val bounds = findBounds(offset.value, anchors.keys)
            val from: ExpandableBoxStateValue
            val to: ExpandableBoxStateValue
            val fromToProgress: Float
            when (bounds.size) {
                0 -> {
                    from = completedValue
                    to = completedValue
                    fromToProgress = 1f
                }
                1 -> {
                    from = anchors.getValue(bounds[0])
                    to = anchors.getValue(bounds[0])
                    fromToProgress = 1f
                }
                else -> {
                    val (a, b) =
                        if (direction > 0f) {
                            bounds[0] to bounds[1]
                        } else {
                            bounds[1] to bounds[0]
                        }
                    from = anchors.getValue(a)
                    to = anchors.getValue(b)
                    fromToProgress = (offset.value - a) / (b - a)
                }
            }
            return ExpandableBoxSwipeProgress(from, to, fromToProgress)
        }

    val direction: Float
        get() = anchors.getOffset(completedValue)?.let { sign(offset.value - it) } ?: 0f

    suspend fun snapTo(targetValue: ExpandableBoxStateValue) {
        latestNonEmptyAnchorsFlow.collect { anchors ->
            val targetOffset = anchors.getOffset(targetValue)
            requireNotNull(targetOffset) {
                "The target value must have an associated anchor."
            }
            snapInternalToOffset(targetOffset)
            completedValue = targetValue
        }
    }

    suspend fun animateTo(targetValue: ExpandableBoxStateValue, anim: AnimationSpec<Float> = animationSpec) {
        latestNonEmptyAnchorsFlow.collect { anchors ->
            try {
                val targetOffset = anchors.getOffset(targetValue)
                requireNotNull(targetOffset) {
                    "The target value must have an associated anchor."
                }
                animateInternalToOffset(targetOffset, anim)
            } finally {
                val endOffset = absoluteOffset.floatValue
                val endValue = anchors
                    // fighting rounding error once again, anchor should be as close as 0.5 pixels
                    .filterKeys { anchorOffset -> abs(anchorOffset - endOffset) < 0.5f }
                    .values.firstOrNull() ?: completedValue
                completedValue = endValue
            }
        }
    }

    suspend fun performFling(velocity: Float) {
        latestNonEmptyAnchorsFlow.collect { anchors ->
            val lastAnchor = anchors.getOffset(completedValue)!!
            val targetValue = computeTarget(
                offset = offset.value,
                lastValue = lastAnchor,
                anchors = anchors.keys,
                thresholds = thresholds,
                velocity = velocity,
                velocityThreshold = velocityThreshold
            )
            val targetState = anchors[targetValue]
            if (targetState != null && confirmStateChange(targetState)) animateTo(targetState)
            // If the user vetoed the state change, rollback to the previous state.
            else animateInternalToOffset(lastAnchor, animationSpec)
        }
    }

    fun performDrag(delta: Float): Float {
        val potentiallyConsumed = absoluteOffset.floatValue + delta
        val clamped = potentiallyConsumed.coerceIn(minBound, maxBound)
        val deltaToConsume = clamped - absoluteOffset.floatValue
        if (abs(deltaToConsume) > 0) {
            draggableState.dispatchRawDelta(deltaToConsume)
        }
        return deltaToConsume
    }

    companion object {
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (ExpandableBoxStateValue) -> Boolean
        ) = Saver<ExpandableBoxState, ExpandableBoxStateValue>(
            save = { it.completedValue },
            restore = { ExpandableBoxState(it, animationSpec, confirmStateChange) }
        )
    }
}

@Immutable
class ExpandableBoxSwipeProgress(
    val from: ExpandableBoxStateValue,
    val to: ExpandableBoxStateValue,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    val fromToProgress: Float
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExpandableBoxSwipeProgress) return false
        return !(from != other.from || to != other.to || fromToProgress != other.fromToProgress)
    }

    override fun hashCode(): Int {
        var result = from.hashCode()
        result = 31 * result + (to.hashCode())
        result = 31 * result + fromToProgress.hashCode()
        return result
    }

    override fun toString(): String {
        return "ExpandableBoxSwipeProgress(from=$from, to=$to, fraction=$fromToProgress)"
    }
}

/**
 * Create and [remember] a [ExpandableBoxState] with the default animation clock.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
fun rememberExpandableBoxState(
    initialValue: ExpandableBoxStateValue,
    animationSpec: AnimationSpec<Float> = AnimationSpec,
    confirmStateChange: (newValue: ExpandableBoxStateValue) -> Boolean = { true }
): ExpandableBoxState {
    return rememberSaveable(
        saver = ExpandableBoxState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    ) {
        ExpandableBoxState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}

internal fun Modifier.expandableBoxSwipeable(
    state: ExpandableBoxState,
    anchors: Map<Float, ExpandableBoxStateValue>,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    thresholds: (from: ExpandableBoxStateValue, to: ExpandableBoxStateValue) -> ThresholdConfig = { _, _ -> FixedThreshold(56.dp) },
    resistance: ResistanceConfig? = resistanceConfig(anchors.keys),
    velocityThreshold: Dp = VelocityThreshold
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "expandableBoxSwipeable"
        properties["state"] = state
        properties["anchors"] = anchors
        properties["orientation"] = orientation
        properties["enabled"] = enabled
        properties["reverseDirection"] = reverseDirection
        properties["interactionSource"] = interactionSource
        properties["thresholds"] = thresholds
        properties["resistance"] = resistance
        properties["velocityThreshold"] = velocityThreshold
    }
) {
    require(anchors.isNotEmpty()) {
        "You must have at least one anchor."
    }
    require(anchors.values.distinct().count() == anchors.size) {
        "You cannot have two anchors mapped to the same state."
    }
    val density = LocalDensity.current
    state.ensureInit(anchors)
    LaunchedEffect(anchors, state) {
        val oldAnchors = state.anchors
        state.anchors = anchors
        state.resistance = resistance
        state.thresholds = { a, b ->
            val from = anchors.getValue(a)
            val to = anchors.getValue(b)
            with(thresholds(from, to)) { density.computeThreshold(a, b) }
        }
        with(density) {
            state.velocityThreshold = velocityThreshold.toPx()
        }
        state.processNewAnchors(oldAnchors, anchors)
    }

    this.draggable(
        orientation = orientation,
        enabled = enabled,
        reverseDirection = reverseDirection,
        interactionSource = interactionSource,
        startDragImmediately = state.isAnimationRunning,
        onDragStopped = { velocity -> launch { state.performFling(velocity) } },
        state = state.draggableState
    )
}

/**
 * Interface to compute a threshold between two anchors/states in a [expandableBoxSwipeable].
 *
 * To define a [ThresholdConfig], consider using [FixedThreshold] and [FractionalThreshold].
 */
@Stable
interface ThresholdConfig {
    /**
     * Compute the value of the threshold (in pixels), once the values of the anchors are known.
     */
    fun Density.computeThreshold(fromValue: Float, toValue: Float): Float
}

/**
 * A fixed threshold will be at an [offset] away from the first anchor.
 *
 * @param offset The offset (in dp) that the threshold will be at.
 */
@Immutable
data class FixedThreshold(private val offset: Dp) : ThresholdConfig {
    override fun Density.computeThreshold(fromValue: Float, toValue: Float): Float {
        return fromValue + offset.toPx() * sign(toValue - fromValue)
    }
}

/**
 * A fractional threshold will be at a [fraction] of the way between the two anchors.
 *
 * @param fraction The fraction (between 0 and 1) that the threshold will be at.
 */
@Immutable
data class FractionalThreshold(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    private val fraction: Float
) : ThresholdConfig {
    override fun Density.computeThreshold(fromValue: Float, toValue: Float): Float {
        return lerp(fromValue, toValue, fraction)
    }
}

/**
 * Specifies how resistance is calculated in [expandableBoxSwipeable].
 *
 * There are two things needed to calculate resistance: the resistance basis determines how much
 * overflow will be consumed to achieve maximum resistance, and the resistance factor determines
 * the amount of resistance (the larger the resistance factor, the stronger the resistance).
 *
 * The resistance basis is usually either the size of the component which [expandableBoxSwipeable] is applied
 * to, or the distance between the minimum and maximum anchors. For a constructor in which the
 * resistance basis defaults to the latter, consider using [resistanceConfig].
 *
 * You may specify different resistance factors for each bound. Consider using one of the default
 * resistance factors in [ExpandableBoxSwipeableDefaults]: `StandardResistanceFactor` to convey that the user
 * has run out of things to see, and `StiffResistanceFactor` to convey that the user cannot swipe
 * this right now. Also, you can set either factor to 0 to disable resistance at that bound.
 *
 * @param basis Specifies the maximum amount of overflow that will be consumed. Must be positive.
 * @param factorAtMin The factor by which to scale the resistance at the minimum bound.
 * Must not be negative.
 * @param factorAtMax The factor by which to scale the resistance at the maximum bound.
 * Must not be negative.
 */
@Immutable
class ResistanceConfig(
    /*@FloatRange(from = 0.0, fromInclusive = false)*/
    val basis: Float,
    /*@FloatRange(from = 0.0)*/
    val factorAtMin: Float = StandardResistanceFactor,
    /*@FloatRange(from = 0.0)*/
    val factorAtMax: Float = StandardResistanceFactor
) {
    fun computeResistance(overflow: Float): Float {
        val factor = if (overflow < 0) factorAtMin else factorAtMax
        if (factor == 0f) return 0f
        val progress = (overflow / basis).coerceIn(-1f, 1f)
        return basis / factor * sin(progress * PI.toFloat() / 2)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResistanceConfig) return false

        if (basis != other.basis) return false
        if (factorAtMin != other.factorAtMin) return false
        if (factorAtMax != other.factorAtMax) return false

        return true
    }

    override fun hashCode(): Int {
        var result = basis.hashCode()
        result = 31 * result + factorAtMin.hashCode()
        result = 31 * result + factorAtMax.hashCode()
        return result
    }

    override fun toString(): String {
        return "ResistanceConfig(basis=$basis, factorAtMin=$factorAtMin, factorAtMax=$factorAtMax)"
    }
}

private fun findBounds(
    offset: Float,
    anchors: Set<Float>
): List<Float> {
    // Find the anchors the target lies between with a little bit of rounding error.
    val a = anchors.filter { it <= offset + 0.001 }.maxOrNull()
    val b = anchors.filter { it >= offset - 0.001 }.minOrNull()

    return when {
        a == null ->
            // case 1 or 3
            listOfNotNull(b)
        b == null ->
            // case 4
            listOf(a)
        a == b ->
            // case 2
            // Can't return offset itself here since it might not be exactly equal
            // to the anchor, despite being considered an exact match.
            listOf(a)
        else ->
            // case 5
            listOf(a, b)
    }
}

private fun computeTarget(
    offset: Float,
    lastValue: Float,
    anchors: Set<Float>,
    thresholds: (Float, Float) -> Float,
    velocity: Float,
    velocityThreshold: Float
): Float {
    val bounds = findBounds(offset, anchors)
    return when (bounds.size) {
        0 -> lastValue
        1 -> bounds[0]
        else -> {
            val lower = bounds[0]
            val upper = bounds[1]
            if (lastValue <= offset) {
                // Swiping from lower to upper (positive).
                if (velocity >= velocityThreshold) {
                    return upper
                } else {
                    val threshold = thresholds(lower, upper)
                    if (offset < threshold) lower else upper
                }
            } else {
                // Swiping from upper to lower (negative).
                if (velocity <= -velocityThreshold) {
                    return lower
                } else {
                    val threshold = thresholds(upper, lower)
                    if (offset > threshold) upper else lower
                }
            }
        }
    }
}

private fun <T> Map<Float, T>.getOffset(state: T): Float? {
    return entries.firstOrNull { it.value == state }?.key
}

object ExpandableBoxSwipeableDefaults {
    val AnimationSpec = SpringSpec<Float>()
    val VelocityThreshold = 125.dp
    const val StiffResistanceFactor = 20f
    const val StandardResistanceFactor = 10f
    fun resistanceConfig(
        anchors: Set<Float>,
        factorAtMin: Float = StandardResistanceFactor,
        factorAtMax: Float = StandardResistanceFactor
    ): ResistanceConfig? {
        return if (anchors.size <= 1) {
            null
        } else {
            val basis = anchors.maxOrNull()!! - anchors.minOrNull()!!
            ResistanceConfig(basis, factorAtMin, factorAtMax)
        }
    }
}
