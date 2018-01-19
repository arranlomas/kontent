package com.arranlomas.mvisample.todolist.functions

import com.arranlomas.mvisample.todolist.objects.TodoListResult
import com.arranlomas.mvisample.todolist.objects.TodoListViewState
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 5/12/2017.
 */
internal val listReducer = BiFunction <TodoListViewState, TodoListResult, TodoListViewState> {
    previousState: TodoListViewState, resultTodo: TodoListResult ->
    when (resultTodo) {
        is TodoListResult.ListLoadSuccess -> previousState.copy(items = resultTodo.items, loading = false)
        is TodoListResult.Error -> previousState.copy(items = emptyList(), loading = false, error = resultTodo.error)
        is TodoListResult.ListLoadInflight -> previousState.copy(items = emptyList(), loading = true, error = null)
    }
}