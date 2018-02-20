package com.arranlomas.mvisample.ui.todolist.objects

import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.mvisample.models.TodoItem

sealed class TodoListResult: KontentResult() {
    data class ListLoadSuccess(val items: List<TodoItem>) : TodoListResult()
    data class Error(val error: Throwable) : TodoListResult()
    class ListLoadInflight : TodoListResult()
}