package com.arranlomas.mvisample.list.objects

import com.arranlomas.kontent.commons.KontentContract
import com.arranlomas.mvisample.models.TodoItemState

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListIntent : KontentContract.Intent {
    class LoadTodoListItems : TodoListIntent()
    data class ChangeItemStatus(val itemId: Long, val itemStatus: TodoItemState) : TodoListIntent()
}
