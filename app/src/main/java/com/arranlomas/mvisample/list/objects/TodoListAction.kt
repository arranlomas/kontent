package com.arranlomas.mvisample.list.objects

import com.arranlomas.mvisample.models.TodoItemState

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListAction {
    class LoadItems : TodoListAction()
    data class ChangeItemStatus(val itemId: Long, val itemStatus: TodoItemState) : TodoListAction()
}