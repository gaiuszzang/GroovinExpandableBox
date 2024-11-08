package io.groovin.expandablebox.sampleapp.music

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Transition
import androidx.constraintlayout.compose.layoutId
import io.groovin.expandablebox.ExpandableBoxStateValue
import io.groovin.expandablebox.sampleapp.R


@OptIn(ExperimentalMotionApi::class)
@Composable
fun MusicPlayerScreen(
    modifier: Modifier = Modifier,
    selectedItemIndex: Int,
    progress: Float,
    progressState: ExpandableBoxStateValue,
    minimizedHeight: Dp,
    foldClick: () -> Unit,
    playClick: () -> Unit
) {
    val statusBarPaddingValues = WindowInsets.statusBars.asPaddingValues()
    val constraintSets = remember(progressState) { getConstraintSets(progressState, minimizedHeight, statusBarPaddingValues) }
    MotionLayout(
        start = constraintSets.first,
        end = constraintSets.second,
        transition = getTransition(progressState),
        progress = progress,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        val imagePadding = if (progressState != ExpandableBoxStateValue.Fold && progressState != ExpandableBoxStateValue.Folding) ((progress * 10) + 10).dp else 10.dp
        val cornerRadius = if (progressState != ExpandableBoxStateValue.Fold && progressState != ExpandableBoxStateValue.Folding) ((progress * 10) + 8).dp else 8.dp
        IconButton(
            modifier = Modifier
                .layoutId("foldButton"),
            onClick = { foldClick() }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        Image(
            modifier = Modifier
                .layoutId("poster")
                .aspectRatio(1f)
                .padding(imagePadding)
                .clip(RoundedCornerShape(cornerRadius)),
            painter = painterResource(id = R.drawable.sample_poster),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .layoutId("songTitle"),
            text = "Music Song : $selectedItemIndex Item"
        )
        IconButton(
            modifier = Modifier
                .layoutId("playButton"),
            onClick = { playClick() }
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null
            )
        }
    }
}

private fun getConstraintSets(state: ExpandableBoxStateValue, foldHeight: Dp, statusBarPaddingValues: PaddingValues) : Pair<ConstraintSet, ConstraintSet> {
    return when(state) {
        ExpandableBoxStateValue.Fold, ExpandableBoxStateValue.Folding -> Pair(hideConstraintSet(foldHeight), foldConstraintSet())
        else -> Pair(foldConstraintSet(), expandConstraintSet(statusBarPaddingValues))
    }
}

@OptIn(ExperimentalMotionApi::class)
private fun getTransition(state: ExpandableBoxStateValue): Transition? {
    return when(state) {
        ExpandableBoxStateValue.Fold, ExpandableBoxStateValue.Folding -> null
        else -> foldExpandTransition()
    }
}

@SuppressLint("Range")
private fun hideConstraintSet(foldHeight: Dp) = ConstraintSet {
    val poster = createRefFor("poster")
    val songTitle = createRefFor("songTitle")
    val foldButton = createRefFor("foldButton")
    val playButton = createRefFor("playButton")
    constrain(poster) {
        width = Dimension.value(40.dp)
        height = Dimension.value(40.dp)
        translationY = foldHeight / 2
        start.linkTo(parent.start)
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
        alpha = 0f
    }
    constrain(foldButton) {
        translationY = foldHeight / 2
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        alpha = 0f
    }
    constrain(playButton) {
        translationY = foldHeight / 2
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
        end.linkTo(parent.end)
        alpha = 0f
    }
    constrain(songTitle) {
        translationY = foldHeight / 2
        width = Dimension.fillToConstraints
        start.linkTo(poster.end)
        end.linkTo(playButton.start)
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
        alpha = 0f
    }
}

@SuppressLint("Range")
private fun foldConstraintSet() = ConstraintSet {
    val poster = createRefFor("poster")
    val songTitle = createRefFor("songTitle")
    val foldButton = createRefFor("foldButton")
    val playButton = createRefFor("playButton")
    constrain(poster) {
        width = Dimension.value(60.dp)
        height = Dimension.value(60.dp)
        start.linkTo(parent.start)
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
        alpha = 1f
    }
    constrain(foldButton) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        alpha = 0f
    }
    constrain(playButton) {
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
        end.linkTo(parent.end)
    }
    constrain(songTitle) {
        width = Dimension.fillToConstraints
        start.linkTo(poster.end)
        end.linkTo(playButton.start)
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
    }
}

private fun expandConstraintSet(statusBarPaddingValues: PaddingValues) = ConstraintSet {
    val poster = createRefFor("poster")
    val songTitle = createRefFor("songTitle")
    val foldButton = createRefFor("foldButton")
    val playButton = createRefFor("playButton")
    constrain(poster) {
        width = Dimension.percent(0.7f)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
    }
    constrain(foldButton) {
        top.linkTo(parent.top, margin = statusBarPaddingValues.calculateTopPadding())
        start.linkTo(parent.start)
        alpha = 1f
    }
    constrain(songTitle) {
        width = Dimension.wrapContent
        top.linkTo(poster.bottom)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
    constrain(playButton) {
        top.linkTo(songTitle.bottom)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }

}

@OptIn(ExperimentalMotionApi::class)
private fun foldExpandTransition(): Transition {
    return Transition {
        val poster = createRefFor("poster")
        /*
        keyAttributes(
            targets = arrayOf(poster)
        ) {
            frame(50) {
                scaleX = 0.5f
                scaleY = 0.5f
            }
        }*/
        keyPositions(
            targets = arrayOf(poster)
        ) {
            frame(50) {
                this.percentX = 0f
                this.percentWidth = 0.2f
                this.percentHeight = 0.2f
            }
        }
    }
}
