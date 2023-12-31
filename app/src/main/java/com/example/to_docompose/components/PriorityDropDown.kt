package com.example.to_docompose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.example.to_docompose.ui.theme.PRIORITY_INDICATOR_SIZE

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                parentSize = it.size
            }
            .background(color = MaterialTheme.colors.background)
            .height(PRIORITY_DROP_DOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            onDraw = { drawCircle(color = priority.color) },
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        )

        Text(
            text = priority.name,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(8f)
        )

        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .alpha(alpha = ContentAlpha.medium)
                .rotate(degrees = angle)
                .weight(1.5f)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.drop_down_arrow_icon)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { parentSize.width.toDp() })
        ) {
            Priority
                .values()
                .slice(0..2)
                .forEach { priority ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onPrioritySelected(priority)
                        }
                    ) {
                        PriorityItem(priority = priority)
                    }
                }
        }
    }
}

@Composable
@Preview
fun PriorityDropDownPreview() {
    PriorityDropDown(
        priority = Priority.LOW,
        onPrioritySelected = {}
    )
}