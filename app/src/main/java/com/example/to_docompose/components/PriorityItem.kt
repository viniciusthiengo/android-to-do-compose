package com.example.to_docompose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.LARGE_PADDING
import com.example.to_docompose.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.to_docompose.ui.theme.Typography

@Composable
fun PriorityItem(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier.size(PRIORITY_INDICATOR_SIZE),
            onDraw = {
                drawCircle(color = priority.color)
            }
        )

        Text(
            text = priority.name,
            style = Typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(start = LARGE_PADDING)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PriorityItemPreview() {
    PriorityItem(priority = Priority.HIGH)
}