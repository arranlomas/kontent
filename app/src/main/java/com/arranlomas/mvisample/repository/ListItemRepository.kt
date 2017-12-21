package com.arranlomas.mvisample.repository

import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.models.TodoItemState
import io.reactivex.Observable

/**
 * Created by arran on 6/12/2017.
 */
internal class ListItemRepository : IListItemRepository {
    private val items = mutableListOf<MutableTodoItem>()

    init {
        (0..10)
                .map { MutableTodoItem(it.toLong(), TodoItemState.ACTIVE, "Todo Item $it", "Description for item $it") }
                .map { items.add(it) }
    }

    override fun getListItems(): Observable<List<TodoItem>> {
        return Observable.just(items.mapToImutable())
    }

    override fun changeItemState(itemId: Long, itemState: TodoItemState): Observable<TodoItem> {
        items.forEach {
            if (it.serverId == itemId) {
                it.state = itemState
                return Observable.just(it.mapToImutable())
            }
        }
        return Observable.error { Throwable("No Item Found") }
    }
}

private fun MutableTodoItem.mapToImutable(): TodoItem{
    return TodoItem(serverId, state, title, description)
}

private fun MutableList<MutableTodoItem>.mapToImutable(): List<TodoItem>{
    val result = mutableListOf<TodoItem>()
    forEach {
        result.add(it.mapToImutable())
    }
    return result
}

private data class MutableTodoItem(var serverId: Long,
                                   var state: TodoItemState,
                                   var title: String,
                                   var description: String)