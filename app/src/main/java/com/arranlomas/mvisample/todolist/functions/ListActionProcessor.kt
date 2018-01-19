package com.arranlomas.mvisample.todolist.functions

import com.arranlomas.kontent.commons.functions.KontentSimpleProcessor
import com.arranlomas.kontent.extensions.composeIo
import com.arranlomas.mvisample.repository.IListItemRepository
import com.arranlomas.mvisample.todolist.objects.TodoListAction
import com.arranlomas.mvisample.todolist.objects.TodoListResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

internal fun listActionProcessor(listItemRepository: IListItemRepository): ObservableTransformer<TodoListAction, TodoListResult> =
        ObservableTransformer { action: Observable<TodoListAction> ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(TodoListAction.LoadItems::class.java).compose(loadItemsLoadProcessor(listItemRepository)),
                        shared.ofType(TodoListAction.ChangeItemStatus::class.java).compose(changeItemStatusProcessor(listItemRepository))
                )
            }
        }


private fun loadItemsLoadProcessor(listItemRepository: IListItemRepository) = KontentSimpleProcessor<TodoListAction.LoadItems, TodoListResult> {
    listItemRepository.getListItems()
            .map {
                TodoListResult.ListLoadSuccess(it) as TodoListResult
            }
            .onErrorReturn { TodoListResult.Error(it) }
            .composeIo()
            .startWith(TodoListResult.ListLoadInflight())
}

private fun changeItemStatusProcessor(listItemRepository: IListItemRepository) = KontentSimpleProcessor<TodoListAction.ChangeItemStatus, TodoListResult> {
    listItemRepository.changeItemState(it.itemId, it.itemStatus)
            .flatMap { listItemRepository.getListItems() }
            .map {
                TodoListResult.ListLoadSuccess(it) as TodoListResult
            }
            .onErrorReturn { TodoListResult.Error(it) }
            .composeIo()
            .startWith(TodoListResult.ListLoadInflight())
}
