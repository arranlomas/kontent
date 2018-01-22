package com.arranlomas.mvisample.ui.todolist

import com.arranlomas.kontent.commons.objects.mvi.KontentContract
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState

interface TodoListContract {
    interface Interactor : KontentContract.Interactor<TodoListIntent, TodoListViewState>
}