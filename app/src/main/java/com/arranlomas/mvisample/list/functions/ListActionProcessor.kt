package com.arranlomas.mvisample.list.functions

import com.arranlomas.kontent.extensions.composeIo
import com.arranlomas.mvisample.list.objects.TodoListAction
import com.arranlomas.mvisample.list.objects.TodoListResult
import com.arranlomas.mvisample.repository.IListItemRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by arran on 5/12/2017.
 */
internal fun listActionProcessor(listItemRepository: IListItemRepository): ObservableTransformer<TodoListAction, TodoListResult> =
        ObservableTransformer { action: Observable<TodoListAction> ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(TodoListAction.LoadItems::class.java).compose(loadItemsLoadProcessor(listItemRepository)),
                        shared.ofType(TodoListAction.ChangeItemStatus::class.java).compose(changeItemStatusProcessor(listItemRepository))
                )
            }
        }

private fun loadItemsLoadProcessor(listItemRepository: IListItemRepository): ObservableTransformer<TodoListAction, TodoListResult> = ObservableTransformer { action ->
    action.switchMap {
        listItemRepository.getListItems()
                .map {
                    TodoListResult.ListLoadSuccess(it) as TodoListResult
                }
                .onErrorReturn { TodoListResult.Error(it) }
                .composeIo()
                .startWith(TodoListResult.ListLoadInflight())
    }
}

private fun changeItemStatusProcessor(listItemRepository: IListItemRepository): ObservableTransformer<TodoListAction.ChangeItemStatus, TodoListResult> = ObservableTransformer { action ->
    action.switchMap {
        listItemRepository.changeItemState(it.itemId, it.itemStatus)
                .flatMap { listItemRepository.getListItems() }
                .map {
                    TodoListResult.ListLoadSuccess(it) as TodoListResult
                }
                .onErrorReturn { TodoListResult.Error(it) }
                .composeIo()
                .startWith(TodoListResult.ListLoadInflight())
    }
}