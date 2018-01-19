package com.arranlomas.mvisample.ui.todolist

import com.arranlomas.kontent.commons.objects.mvi.KontentContract
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState

/**
 * Created by arran on 5/12/2017.
 */
internal interface TodoListContract {
    interface Interactor : KontentContract.Interactor<TodoListViewState, TodoListIntent>
}