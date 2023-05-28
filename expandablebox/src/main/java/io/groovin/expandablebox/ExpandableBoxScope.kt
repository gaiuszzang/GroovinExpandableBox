package io.groovin.expandablebox

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

data class ExpandableBoxScope(
    val progress: Float,
    val progressState: ExpandableBoxState,
    val completedState: ExpandableBoxState,
    private val innerBoxScope: BoxScope
): BoxScope {
    @Stable
    override fun Modifier.align(alignment: Alignment): Modifier = with(innerBoxScope) {
        this@align.align(alignment)
    }

    @Stable
    override fun Modifier.matchParentSize(): Modifier = with(innerBoxScope) {
        this@matchParentSize.matchParentSize()
    }
}

