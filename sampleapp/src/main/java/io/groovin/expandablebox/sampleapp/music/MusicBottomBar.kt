package io.groovin.expandablebox.sampleapp.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun MusicBottomBar(
    modifier: Modifier = Modifier,
    onMenuClick: ((Int) -> Unit)? = null
) {
    Row(
        modifier = modifier
    ) {
        MusicBottomBarItem(imageVector = Icons.Default.PlayArrow) {
            onMenuClick?.invoke(0)
        }
        MusicBottomBarItem(imageVector = Icons.Default.Favorite) {
            onMenuClick?.invoke(1)
        }
        MusicBottomBarItem(imageVector = Icons.Default.AccountCircle) {
            onMenuClick?.invoke(2)
        }
    }
}

@Composable
private fun RowScope.MusicBottomBarItem(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterHorizontally),
            imageVector = imageVector,
            contentDescription = null
        )
    }
}
