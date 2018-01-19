package com.arranlomas.mvisample.todolist.functions

import com.arranlomas.kontent.commons.functions.KontentActionToResultProcessor
import com.arranlomas.kontent.commons.functions.KontentSimpleNetworkRequestProcessor
import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.repository.IListItemRepository
import com.arranlomas.mvisample.todolist.objects.TodoListAction
import com.arranlomas.mvisample.todolist.objects.TodoListResult
import io.reactivex.Observable

/**
 * TODO - extract this to a base component that only compiles if all actions are covered like a when
 */
internal fun listActionProcessor(listItemRepository: IListItemRepository) = KontentActionToResultProcessor<TodoListAction, TodoListResult> { actionObservable ->
    Observable.merge(
            actionObservable.ofType(TodoListAction.LoadItems::class.java).compose(loadItemsLoadProcessor(listItemRepository)),
            actionObservable.ofType(TodoListAction.ChangeItemStatus::class.java).compose(changeItemStatusProcessor(listItemRepository))
    )
}

private fun loadItemsLoadProcessor(listItemRepository: IListItemRepository) = KontentSimpleNetworkRequestProcessor<TodoListAction.LoadItems, TodoListResult, List<TodoItem>>(
        networkRequest = { listItemRepository.getListItems() },
        success = { TodoListResult.ListLoadSuccess(it) },
        error = { TodoListResult.Error(it) },
        loading = TodoListResult.ListLoadInflight())

private fun changeItemStatusProcessor(listItemRepository: IListItemRepository) = KontentSimpleNetworkRequestProcessor<TodoListAction.ChangeItemStatus, TodoListResult, List<TodoItem>>(
        networkRequest = { action ->
            listItemRepository.changeItemState(action.itemId, action.itemStatus)
                    .flatMap { listItemRepository.getListItems() }
        },
        success = { TodoListResult.ListLoadSuccess(it) },
        error = { TodoListResult.Error(it) },
        loading = TodoListResult.ListLoadInflight())
