package com.example.to_docompose.ui.screens.splash

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.LOGO_HEIGHT
import com.example.to_docompose.ui.theme.ToDoComposeTheme
import com.example.to_docompose.ui.theme.splashScreenBackground
import com.example.to_docompose.util.Constants.SPLASH_SCREEN_DELAY
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToTaskScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val offsetState by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 100.dp,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(
        key1 = true,
        block = {
            startAnimation = true
            delay(timeMillis = SPLASH_SCREEN_DELAY)
            navigateToTaskScreen()
        }
    )

    Splash(
        offsetState = offsetState,
        alphaState = alphaState,
        modifier = modifier
    )
}

@Composable
fun Splash(
    offsetState: Dp,
    alphaState: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.splashScreenBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = getLogo()),
            contentDescription = stringResource(R.string.todo_logo),
            modifier = Modifier
                .size(LOGO_HEIGHT)
                .offset(y = offsetState)
                .alpha(alpha = alphaState)
        )
    }
}

@Composable
private fun getLogo(): Int =
    if (isSystemInDarkTheme()) {
        R.drawable.logo_dark
    } else {
        R.drawable.logo_light
    }

@Composable
@Preview
fun SplashScreenPreview() {
    ToDoComposeTheme {
        Splash(
            offsetState = 0.dp,
            alphaState = 1f,
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun SplashScreenPreviewDark() {
    ToDoComposeTheme {
        Splash(
            offsetState = 0.dp,
            alphaState = 1f,
        )
    }
}