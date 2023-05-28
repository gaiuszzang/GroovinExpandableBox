package io.groovin.expandablebox.sampleapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    val navAction = LocalNavAction.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MainHeader()
            MainItem(
                text = "Sample 1 : Music Player (Downside)",
                onClick = {
                    navAction.moveToMusicBottomExpandBox()
                }
            )
            MainItem(
                text = "Sample 2 : Music Player (Upside)",
                onClick = {
                    navAction.moveToMusicUpExpandBox()
                }
            )

            MainItem(
                text = "Sample 2 : Article Page",
                onClick = {
                    navAction.moveToArticleExpandBox()
                }
            )
        }
    }
}

@Composable
private fun MainHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            text = "Expandable Box\nSample List",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MainItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Text(
            modifier = Modifier
                .padding(14.dp)
                .align(Alignment.CenterStart),
            text = text,
            fontSize = 16.sp
        )
    }
}
