package com.arranlomas.mvisample.todolist.objects

import com.arranlomas.kontent.commons.objects.mvi.KontentViewState
import com.arranlomas.mvisample.models.TodoItem

/**
 * Created by arran on 5/12/2017.
 */
internal data class TodoListViewState(
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