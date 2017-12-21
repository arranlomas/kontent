package com.arranlomas.mvisample.repository

import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.models.TodoItemState
import io.reactivex.Observable

/**
 * Created by arran on 6/12/2017.
 */
interface IListItemRepository {
    fun getListItems(): Observable<List<TodoItem>>
    fun changeItemState(itemId: Long, itemState: TodoItemState): Observable<TodoItem>
}