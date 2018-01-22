package com.arranlomas.mvisample.repository

import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.models.TodoItemState
import io.reactivex.Observable

internal class ListItemRepository : IListItemRepository {

    private val items = mutableListOf<MutableTodoItem>()

    init {
        (0..10)
                .map { MutableTodoItem(it.toLong(), TodoItemState.ACTIVE, "Todo Item $it", "Description for item $it") }
                .map { items.add(it) }
    }

    override fun getListItems(): Observable<List<TodoItem>> {
        return Observable.just(items.mapToMutable())
    }

    override fun changeItemState(itemId: Long, itemState: TodoItemState): Observable<TodoItem> {
        items.forEach {
            if (it.serverId == itemId) {
                it.state = itemState
                return Observable.just(it.mapToMutable())
            }
        }
        return Observable.error { Throwable("No Item Found") }
    }

    override fun addItem(title: String, description: String): Observable<Boolean> {
        items.add(MutableTodoItem((items.size+1).toLong(), TodoItemState.ACTIVE, title, description))
        return Observable.just(true)
    }
}

private fun MutableTodoItem.mapToMutable(): TodoItem{
    return TodoItem(serverId, state, title, description)
}

private fun MutableList<MutableTodoItem>.mapToMutable(): List<TodoItem>{
    val result = mutableListOf<TodoItem>()
    forEach {
        result.add(it.mapToMutable())
    }
    return result
}

private data class MutableTodoItem(var serverId: Long,
                                   var state: TodoItemState,
                                   var title: String,
                                   var description: String)