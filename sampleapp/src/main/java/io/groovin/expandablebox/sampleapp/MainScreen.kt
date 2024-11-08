package io.groovin.expandablebox.sampleapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.expandablebox.sampleapp.ui.GroovinOkayCancelDialog
import io.groovin.expandablebox.sampleapp.ui.GroovinSelectableText

@Composable
fun MainScreen() {
    val navAction = LocalNavAction.current
    var isShowMapOptionDialog by remember { mutableStateOf(false) }
    var mapOption by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MainHeader()
            MainItem(
                modifier = Modifier.testTag("MusicPlayerMenu"),
                text = "Sample 1 : Music Player",
                onClick = {
                    navAction.moveToMusicBottomExpandBox()
                }
            )
            MainItem(
                modifier = Modifier.testTag("ArticlePageMenu"),
                text = "Sample 2 : Article Page",
                onClick = {
                    navAction.moveToArticleExpandBox()
                }
            )

            MainItem(
                modifier = Modifier.testTag("MapMenu"),
                text = "Sample 3 : Map",
                onClick = {
                    isShowMapOptionDialog = true
                }
            )
        }
    }
    if (isShowMapOptionDialog) {
        GroovinOkayCancelDialog(
            onPositiveClick = {
                navAction.moveToMapExpandBox(mapOption)
                isShowMapOptionDialog = false
            },
            onCancelClick = {
                isShowMapOptionDialog = false
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                GroovinSelectableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    isSelectable = (mapOption == 0),
                    text = "use Nested Scroll",
                    onClick = {
                        mapOption = 0
                    }
                )
                GroovinSelectableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    isSelectable = (mapOption == 1),
                    text = "conditional Nested Scroll",
                    onClick = {
                        mapOption = 1
                    }
                )
                GroovinSelectableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    isSelectable = (mapOption == 2),
                    text = "disable Nested Scroll",
                    onClick = {
                        mapOption = 2
                    }
                )
            }
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
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
