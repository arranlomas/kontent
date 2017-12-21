package com.arranlomas.mvisample.list.objects

import com.arranlomas.mvisample.models.TodoItem

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListResult {
    data class ListLoadSuccess(val items: List<TodoItem>) : TodoListResult()
    data class Error(val error: Throwable) : TodoListResult()
    class ListLoadInflight : TodoListResult()
}