package com.example.to_docompose.util

object Constants {
    const val DATABASE_NAME = "todo_database"
    const val DATABASE_TABLE = "todo_table"

    const val LIST_ARGUMENT_KEY = "action"
    const val LIST_SCREEN = "list/{$LIST_ARGUMENT_KEY}"

    const val TASK_ARGUMENT_KEY = "taskId"
    const val TASK_SCREEN = "task/{$TASK_ARGUMENT_KEY}"

    const val MAX_TITLE_LENGTH = 20

    const val PREFERENCE_NAME = "todo_preferences"
    const val PREFERENCE_KEY = "sort_state"
}