package com.arranlomas.mvisample.models

data class TodoItem (val serverId: Long,
                     val state: TodoItemState,
                     val title: String,
                     val description: String)