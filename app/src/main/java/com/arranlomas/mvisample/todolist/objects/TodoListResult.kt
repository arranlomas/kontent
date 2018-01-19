package com.arranlomas.mvisample.todolist.objects

import com.arranlomas.kontent.commons.KontentResult
import com.arranlomas.mvisample.models.TodoItem

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListResult: KontentResult() {
    data class ListLoadSuccess(val items: List<TodoItem>) : TodoListResult()
    data class Error(val error: Throwable) : TodoListResult()
    class ListLoadInflight : TodoListResult()
}