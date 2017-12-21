package com.arranlomas.mvisample.list.objects

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListAction {
    class LoadItems : TodoListAction()
}