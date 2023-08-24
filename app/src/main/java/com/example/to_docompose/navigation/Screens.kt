package com.example.to_docompose.navigation

import androidx.navigation.NavController
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.Constants.LIST_SCREEN

class Screens(navController: NavController) {
    val list: (Action) -> Unit = { action ->
        navController.navigate(route = "list/${action.name}") {
            popUpTo(route = LIST_SCREEN) {
                inclusive = true
            }
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navController.navigate(route = "task/$taskId")
    }
}