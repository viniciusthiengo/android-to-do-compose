package com.example.to_docompose.navigation

import androidx.navigation.NavController
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.Constants.LIST_SCREEN
import com.example.to_docompose.util.Constants.SPLASH_SCREEN

class Screens(navController: NavController) {
    val splash: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION.name}") {
            popUpTo(route = SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate(route = "task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate(route = "list/${action.name}") {
            popUpTo(route = LIST_SCREEN) {
                inclusive = true
            }
        }
    }
}