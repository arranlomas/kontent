package com.arranlomas.mvisample.repository

import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.models.TodoItemState
import io.reactivex.Observable

/**
 * Created by arran on 6/12/2017.
 */
internal class ListItemRepository : IListItemRepository {
    override fun getListItems(): Observable<List<TodoItem>> {
        val list = mutableListOf<TodoItem>()
        (0..10)
                .map { TodoItem(it.toLong(), TodoItemState.ACTIVE, "Todo Item $it", "Description for item $it") }
                .map { list.add(it) }
        return Observable.just(list.toList())
    }
}