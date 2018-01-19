package com.arranlomas.mvisample.todolist.functions

import com.arranlomas.mvisample.todolist.objects.TodoListAction
import com.arranlomas.mvisample.todolist.objects.TodoListIntent

/**
 * Created by arran on 5/12/2017.
 */

internal fun listIntentToAction(intent: TodoListIntent): TodoListAction = when (intent) {
    is TodoListIntent.LoadTodoListItems -> TodoListAction.LoadItems()
    is TodoListIntent.ChangeItemStatus -> TodoListAction.ChangeItemStatus(intent.itemId, intent.itemStatus)
}