package com.arranlomas.mvisample.list.objects

import com.arranlomas.kontent.commons.KontentContract
import com.arranlomas.mvisample.models.TodoItem

/**
 * Created by arran on 5/12/2017.
 */
internal data class TodoListViewState(
        val items: List<TodoItem> = emptyList(),
        val loading: Boolean = false,
        val error: Throwable? = null
) : KontentContract.ViewState {
    companion object Factory {
        fun default(): TodoListViewState {
            return TodoListViewState()
        }
    }
}