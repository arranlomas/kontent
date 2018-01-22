package com.arranlomas.mvisample.repository

import com.arranlomas.mvisample.models.TodoItem
import com.arranlomas.mvisample.models.TodoItemState
import io.reactivex.Observable

interface IListItemRepository {
    fun getListItems(): Observable<List<TodoItem>>
    fun changeItemState(itemId: Long, itemState: TodoItemState): Observable<TodoItem>
    fun addItem(title: String, description: String): Observable<Boolean>
}