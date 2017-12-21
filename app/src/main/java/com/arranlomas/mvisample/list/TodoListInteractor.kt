package com.arranlomas.mvisample.list

import com.arranlomas.kontent.commons.KontentInteractor
import com.arranlomas.mvisample.list.functions.listActionProcessor
import com.arranlomas.mvisample.list.functions.listIntentToAction
import com.arranlomas.mvisample.list.functions.listReducer
import com.arranlomas.mvisample.list.objects.TodoListIntent
import com.arranlomas.mvisample.list.objects.TodoListViewState
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
