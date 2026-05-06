package io.groovin.expandablebox.sampleapp.music

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.theme.Pink40
import io.groovin.expandablebox.sampleapp.ui.theme.Pink80

@Composable
fun MusicProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 48.dp)
                .size(140.dp)
                .clip(CircleShape)
                .background(Pink80.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(120.dp),
                imageVector = Icons.Default.AccountCircle,
                tint = Pink40,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Groovin Listener",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "@groovin_user",
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.55f)
        )
        ProfileStatsRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)
        )
        ProfileInfoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            label = "Member since",
            value = "March 2024"
        )
        ProfileInfoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            label = "Favorite genre",
            value = "Indie / Lo-fi"
        )
    }
}

@Composable
private fun ProfileStatsRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ProfileStat(label = "Playlists", value = "12")
        ProfileStat(label = "Following", value = "84")
        ProfileStat(label = "Followers", value = "37")
    }
}

@Composable
private fun ProfileStat(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Pink40
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = label,
            fontSize = 12.sp,
            color = Color.Black.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ProfileInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Pink80.copy(alpha = 0.25f))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.55f)
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
