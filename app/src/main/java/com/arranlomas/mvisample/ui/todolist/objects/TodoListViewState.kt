package com.arranlomas.mvisample.ui.todolist.objects

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.mvisample.models.TodoItem

data class TodoListViewState(
        val items: List<TodoItem> = emptyList(),
        val loading: Boolean = false,
        val error: Throwable? = null
) : KontentViewState() {
    companion object Factory {
        fun default(): TodoListViewState {
            return TodoListViewState()
        }
    }
}