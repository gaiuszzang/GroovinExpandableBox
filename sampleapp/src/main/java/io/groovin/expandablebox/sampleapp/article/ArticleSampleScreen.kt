package io.groovin.expandablebox.sampleapp.article

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.groovin.expandablebox.ExpandableBox
import io.groovin.expandablebox.ExpandableBoxState
import io.groovin.expandablebox.sampleapp.ui.theme.Pink80
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleSampleScreen() {
    val coroutineScope = rememberCoroutineScope()
    val articleList = remember { getArticleList() }
    var selectedArticle by remember { mutableStateOf(articleList[0]) }
    val foldHeight = remember { 200.dp }
    Box(modifier = Modifier.fillMaxSize()) {
        val swipeableState = rememberSwipeableState(
            initialValue = ExpandableBoxState.FOLD,
            confirmStateChange = {
                true
            }
        )
        ArticleContentScreen(
            modifier = Modifier.fillMaxSize().padding(bottom = 200.dp),
            title = selectedArticle.title,
            content = selectedArticle.contents
        )
        ExpandableBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(top = 100.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(color = Pink80),
            swipeableState = swipeableState,
            isDownDirection = true,
            isHideable = false,
            foldHeight = foldHeight
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
                ArticleListScreen(
                    modifier = Modifier
                        .weight(1f),
                    list = articleList,
                    selectedItem = selectedArticle,
                    onItemClick = {
                        selectedArticle = it
                        coroutineScope.launch {
                            swipeableState.animateTo(ExpandableBoxState.FOLD)
                        }
                    }
                )
            }
            BackHandler(
                enabled = (completedState == ExpandableBoxState.EXPAND || progressState == ExpandableBoxState.EXPAND)
            ) {
                coroutineScope.launch {
                    swipeableState.animateTo(ExpandableBoxState.FOLD)
                }
            }
        }
    }
}

