package com.arranlomas.mvisample.ui.todolist.functions

import com.arranlomas.kontent.commons.functions.KontentProcessor
import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.repository.IListItemRepository
import com.arranlomas.mvisample.ui.todolist.objects.TodoListAction
import com.arranlomas.mvisample.ui.todolist.objects.TodoListResult
import io.reactivex.Observable

/**
 * TODO - extract this to a base component that only compiles if all actions are covered like a when
 */
internal fun listActionProcessor(listItemRepository: IListItemRepository) = KontentProcessor<TodoListAction, TodoListResult> { actionObservable ->
    Observable.merge(
            actionObservable.ofType(TodoListAction.LoadItems::class.java).compose(loadItemsLoadProcessor(listItemRepository)),
            actionObservable.ofType(TodoListAction.ChangeItemStatus::class.java).compose(changeItemStatusProcessor(listItemRepository)),
            actionObservable.ofType(TodoListAction.AddItem::class.java).compose(addItemProcessor(listItemRepository))
    )
}

private fun loadItemsLoadProcessor(listItemRepository: IListItemRepository) = KontentActionProcessor<TodoListAction.LoadItems, TodoListResult, List<TodoItem>>(
        action = { listItemRepository.getListItems() },
        success = { TodoListResult.ListLoadSuccess(it) },
        error = { TodoListResult.Error(it) },
        loading = TodoListResult.ListLoadInflight())

private fun changeItemStatusProcessor(listItemRepository: IListItemRepository) = KontentActionProcessor<TodoListAction.ChangeItemStatus, TodoListResult, List<TodoItem>>(
        action = { action ->
            listItemRepository.changeItemState(action.itemId, action.itemStatus)
                    .flatMap { listItemRepository.getListItems() }
        },
        success = { TodoListResult.ListLoadSuccess(it) },
        error = { TodoListResult.Error(it) },
        loading = TodoListResult.ListLoadInflight())

private fun addItemProcessor(listItemRepository: IListItemRepository) = KontentActionProcessor<TodoListAction.AddItem, TodoListResult, List<TodoItem>>(
        action = { action ->
            listItemRepository.addItem(action.title, action.description)
                    .flatMap { listItemRepository.getListItems() }
        },
        success = { TodoListResult.ListLoadSuccess(it) },
        error = { TodoListResult.Error(it) },
        loading = TodoListResult.ListLoadInflight())
