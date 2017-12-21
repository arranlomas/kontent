package com.arranlomas.mvisample.list.functions

import com.arranlomas.mvisample.list.objects.TodoListAction
import com.arranlomas.mvisample.list.objects.TodoListIntent

/**
 * Created by arran on 5/12/2017.
 */

internal fun listIntentToAction(intent: TodoListIntent): TodoListAction = when (intent) {
    is TodoListIntent.LoadTodoListItems -> TodoListAction.LoadItems()
    is TodoListIntent.ChangeItemStatus -> TodoListAction.ChangeItemStatus(intent.itemId, intent.itemStatus)
}