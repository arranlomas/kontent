package com.arranlomas.mvisample.ui.todolist.functions

import com.arranlomas.mvisample.ui.todolist.objects.TodoListAction
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent

internal fun listIntentToAction(intent: TodoListIntent): TodoListAction = when (intent) {
    is TodoListIntent.LoadTodoListItems -> TodoListAction.LoadItems()
    is TodoListIntent.ChangeItemStatus -> TodoListAction.ChangeItemStatus(intent.itemId, intent.itemStatus)
    is TodoListIntent.AddItem -> TodoListAction.AddItem(intent.title, intent.description)
}