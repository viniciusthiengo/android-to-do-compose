package com.example.to_docompose.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action {
    return when {
        this.equals("ADD", true) -> Action.ADD
        this.equals("UPDATE", true) -> Action.UPDATE
        this.equals("DELETE", true) -> Action.DELETE
        this.equals("DELETE_ALL", true) -> Action.DELETE_ALL
        this.equals("UNDO", true) -> Action.UNDO
        else -> Action.NO_ACTION
    }
}