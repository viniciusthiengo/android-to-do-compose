package com.example.to_docompose.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action =
    if (this.isNullOrEmpty()) {
        Action.NO_ACTION
    } else {
        Action.valueOf(this.uppercase())
    }