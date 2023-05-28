package io.groovin.expandablebox.sampleapp.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    list: List<Article>,
    selectedItem: Article?,
    onItemClick: ((Article) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .then(modifier),
        state = listState
    ) {
        itemsIndexed(list) { index, item ->
            ArticleListItem(
                title = item.title,
                isSelected = (item == selectedItem),
                onClick = {
                    onItemClick?.invoke(item)
                    coroutineScope.launch {
                        delay(100)
                        listState.animateScrollToItem(index)
                    }
                }
            )
        }
    }
}

@Composable
private fun ArticleListItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onClick()
            }
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
            text = title,
            fontSize = 16.sp,
            color = if (isSelected) Color.Blue else colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}
