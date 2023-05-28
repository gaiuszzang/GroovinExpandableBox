package io.groovin.expandablebox.sampleapp.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArticleContentScreen(
    modifier: Modifier = Modifier,
    title: String,
    content: String
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(title) {
        scrollState.scrollTo(0)
    }
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .then(modifier)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.CenterHorizontally),
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .padding(16.dp),
            text = content,
            fontSize = 16.sp
        )
    }
}
