package com.arranlomas.mvisample.ui.todolist

import com.arranlomas.kontent.commons.objects.KontentInteractor
import com.arranlomas.mvisample.repository.IListItemRepository
import com.arranlomas.mvisample.ui.todolist.functions.listActionProcessor
import com.arranlomas.mvisample.ui.todolist.functions.listIntentToAction
import com.arranlomas.mvisample.ui.todolist.functions.listReducer
import com.arranlomas.mvisample.ui.todolist.objects.TodoListAction
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.ui.todolist.objects.TodoListResult
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState

class TodoListInteractor(listItemRepository: IListItemRepository) :
        TodoListContract.Interactor, KontentInteractor<TodoListIntent, TodoListAction, TodoListResult, TodoListViewState>(
        intentToAction = { intent -> listIntentToAction(intent) },
        actionProcessor = listActionProcessor(listItemRepository),
        defaultState = TodoListViewState.default(),
        reducer = listReducer
)
