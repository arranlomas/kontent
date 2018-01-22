package com.arranlomas.mvisample.ui.todolist.objects

import com.arranlomas.kontent.commons.objects.mvi.KontentAction
import com.arranlomas.mvisample.models.TodoItemState

internal sealed class TodoListAction: KontentAction() {
    class LoadItems : TodoListAction()
    data class ChangeItemStatus(val itemId: Long, val itemStatus: TodoItemState) : TodoListAction()
    data class AddItem(val title: String, val description: String): TodoListAction()
}