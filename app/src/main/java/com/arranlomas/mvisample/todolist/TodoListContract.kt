package com.arranlomas.mvisample.todolist

import com.arranlomas.kontent.commons.objects.mvi.KontentContract
import com.arranlomas.mvisample.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.todolist.objects.TodoListViewState

/**
 * Created by arran on 5/12/2017.
 */
internal interface TodoListContract {
    interface Interactor : KontentContract.Interactor<TodoListViewState, TodoListIntent>
}