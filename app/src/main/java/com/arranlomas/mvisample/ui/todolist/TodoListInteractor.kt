package com.arranlomas.mvisample.ui.todolist

import com.arranlomas.kontent.commons.objects.mvi.KontentInteractor
import com.arranlomas.mvisample.ui.todolist.functions.listActionProcessor
import com.arranlomas.mvisample.ui.todolist.functions.listIntentToAction
import com.arranlomas.mvisample.ui.todolist.functions.listReducer
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState
import com.arranlomas.mvisample.repository.IListItemRepository

/**
 * Created by arran on 5/12/2017.
 */
internal class TodoListInteractor(private val listItemRepository: IListItemRepository) : TodoListContract.Interactor, KontentInteractor<TodoListViewState, TodoListIntent>() {
    init {
        super.processor = { intents ->
            intents
                    .map { intent -> listIntentToAction(intent) }
                    .compose(listActionProcessor(listItemRepository))
                    .scan(TodoListViewState.default(), listReducer)
        }
    }
}
