package com.example.to_docompose.ui.screens.list

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.fabBackgroundColor
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(
        key1 = true,
        block = {
            sharedViewModel.getAllTasks()
            sharedViewModel.readSortState()
        }
    )

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val action by sharedViewModel.action
    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = {
            sharedViewModel.handlerDatabaseActions(action = action)
        },
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchedTasks = searchedTasks,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.action.value = action
                    sharedViewModel.updateTaskFields(selectedTask = task)
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(onFabClicked: (taskId: Int) -> Unit) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    val actionLabel = stringResource(getActionLabel(action = action))

    LaunchedEffect(
        key1 = action,
        block = {
            if (action != Action.NO_ACTION) {
                scope.launch {
                    val snackBarResult = scaffoldState
                        .snackbarHostState
                        .showSnackbar(
                            message = getMessage(action = action, taskTitle = taskTitle),
                            actionLabel = actionLabel
                        )

                    undoDeletedTask(
                        action = action,
                        snackBarResult = snackBarResult,
                        onUndoClicked = onUndoClicked
                    )
                }
            }
        }
    )
}

private fun getActionLabel(action: Action): Int =
    if (action == Action.DELETE) {
        R.string.undo
    } else {
        R.string.ok
    }

private fun getMessage(
    action: Action,
    taskTitle: String
): String =
    when (action) {
        Action.DELETE_ALL -> "All tasks removed."
        else -> "${action.name}: $taskTitle"
    }

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed &&
        action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}