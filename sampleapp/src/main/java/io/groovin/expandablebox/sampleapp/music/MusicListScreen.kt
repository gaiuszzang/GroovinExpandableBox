package io.groovin.expandablebox.sampleapp.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.layoutId
import io.groovin.expandablebox.sampleapp.R


@Composable
fun MusicListScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onItemClick: ((Int) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(
            count = 100,
            key = { index -> "MusicSongItem$index" },
            contentType = { _ -> "MusicSongItem" }
        ) { index ->
            MusicListItem(
                title = "Music Song $index Item",
                onClick = {
                    onItemClick?.invoke(index)
                }
            )
        }
    }
}

@Composable
private fun MusicListItem(
    imageId: Int = R.drawable.sample_poster,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(12.dp)
    ) {
        Image(
            modifier = Modifier
                .layoutId("poster")
                .size(48.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.CenterVertically),
            painter = painterResource(id = imageId),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
            text = title,
            fontSize = 16.sp
        )
    }
}
