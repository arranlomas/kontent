package com.arranlomas.mvisample.ui.todolist.objects

import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.mvisample.models.TodoItemState

sealed class TodoListIntent : KontentIntent() {
    class LoadTodoListItems : TodoListIntent()
    data class ChangeItemStatus(val itemId: Long, val itemStatus: TodoItemState) : TodoListIntent()
    data class AddItem(val title: String, val description: String): TodoListIntent()
}