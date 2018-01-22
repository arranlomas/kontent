package com.arranlomas.mvisample.ui.todolist.functions

import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.objects.mvi.KontentResult
import com.arranlomas.kontent.commons.objects.mvi.KontentViewState
import com.arranlomas.mvisample.ui.todolist.objects.TodoListResult
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState
import io.reactivex.functions.BiFunction

val listReducer = KontentReducer<TodoListResult, TodoListViewState>({ result, previousState ->
    when (result) {
        is TodoListResult.ListLoadSuccess -> previousState.copy(items = result.items, loading = false)
        is TodoListResult.Error -> previousState.copy(items = emptyList(), loading = false, error = result.error)
        is TodoListResult.ListLoadInflight -> previousState.copy(items = emptyList(), loading = true, error = null)
    }
})