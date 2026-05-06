package io.groovin.expandablebox.sampleapp.music

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.theme.Pink40

@Composable
fun MusicFavoriteScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(96.dp),
            imageVector = Icons.Outlined.FavoriteBorder,
            tint = Pink40.copy(alpha = 0.7f),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "No favorites yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Tap the heart on a song you love\nto add it to your favorites.",
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}
