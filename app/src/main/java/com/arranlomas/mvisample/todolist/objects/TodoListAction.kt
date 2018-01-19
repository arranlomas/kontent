package com.arranlomas.mvisample.todolist.objects

import com.arranlomas.kontent.commons.KontentAction
import com.arranlomas.mvisample.models.TodoItemState

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListAction: KontentAction() {
    class LoadItems : TodoListAction()
    data class ChangeItemStatus(val itemId: Long, val itemStatus: TodoItemState) : TodoListAction()
}