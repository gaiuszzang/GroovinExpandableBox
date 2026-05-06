package io.groovin.expandablebox.sampleapp.music

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.groovin.expandablebox.sampleapp.ui.theme.Pink40


@Composable
fun MusicBottomBar(
    modifier: Modifier = Modifier,
    selectedTab: Int = 0,
    onTabClick: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier
    ) {
        MusicBottomBarItem(
            imageVector = Icons.AutoMirrored.Filled.List,
            isSelected = selectedTab == 0
        ) {
            onTabClick(0)
        }
        MusicBottomBarItem(
            imageVector = Icons.Default.Favorite,
            isSelected = selectedTab == 1
        ) {
            onTabClick(1)
        }
        MusicBottomBarItem(
            imageVector = Icons.Default.AccountCircle,
            isSelected = selectedTab == 2
        ) {
            onTabClick(2)
        }
    }
}

@Composable
private fun RowScope.MusicBottomBarItem(
    imageVector: ImageVector,
    isSelected: Boolean,
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
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(if (isSelected) Color.White.copy(alpha = 0.7f) else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = imageVector,
                tint = if (isSelected) Pink40 else Color.Black.copy(alpha = 0.5f),
                contentDescription = null
            )
        }
    }
}
