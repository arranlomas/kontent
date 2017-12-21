package com.arranlomas.mvisample.list

import com.arranlomas.kontent.commons.KontentContract
import com.arranlomas.mvisample.list.objects.TodoListIntent
import com.arranlomas.mvisample.list.objects.TodoListViewState

/**
 * Created by arran on 5/12/2017.
 */
internal interface TodoListContract {
    interface Interactor : KontentContract.Interactor<TodoListViewState, TodoListIntent>
}