package com.example.to_docompose.ui.screens.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    LaunchedEffect(
        key1 = true,
        block = {
            delay(timeMillis = SPLASH_SCREEN_DELAY)
            navigateToTaskScreen()
        }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.splashScreenBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = getLogo()),
            contentDescription = stringResource(R.string.todo_logo),
            modifier = Modifier.size(LOGO_HEIGHT)
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
        SplashScreen(navigateToTaskScreen = {})
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun SplashScreenPreviewDark() {
    ToDoComposeTheme {
        SplashScreen(navigateToTaskScreen = {})
    }
}