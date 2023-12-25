package io.groovin.expandablebox.sampleapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GroovinSelectableText(
    isSelectable: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            modifier = Modifier
                .size(18.dp)
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .then(
                    if (isSelectable) {
                        Modifier.padding(4.dp)
                            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    } else {
                        Modifier
                    }
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text
        )
    }
}

@Preview
@Composable
private fun GroovinSelectableTextPreview() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        GroovinSelectableText(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            isSelectable = true,
            text = "I am selected"
        )
        GroovinSelectableText(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            isSelectable = false,
            text = "I am not selected"
        )
    }
}