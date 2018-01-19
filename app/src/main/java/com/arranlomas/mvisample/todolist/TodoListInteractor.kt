package com.arranlomas.mvisample.todolist

import com.arranlomas.kontent.commons.objects.mvi.KontentInteractor
import com.arranlomas.mvisample.todolist.functions.listActionProcessor
import com.arranlomas.mvisample.todolist.functions.listIntentToAction
import com.arranlomas.mvisample.todolist.functions.listReducer
import com.arranlomas.mvisample.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.todolist.objects.TodoListViewState
import com.arranlomas.mvisample.repository.IListItemRepository

/**
 * Created by arran on 5/12/2017.
 */
internal class TodoListInteractor(val listItemRepository: IListItemRepository) : TodoListContract.Interactor, KontentInteractor<TodoListViewState, TodoListIntent>() {
    init {
        super.processor = { intents ->
            intents
                    .map { intent -> listIntentToAction(intent) }
                    .compose(listActionProcessor(listItemRepository))
                    .scan(TodoListViewState.default(), listReducer)
        }
    }
}
