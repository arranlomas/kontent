package com.arranlomas.mvisample.list.objects

import com.arranlomas.kontent.commons.KontentContract

/**
 * Created by arran on 5/12/2017.
 */
internal sealed class TodoListIntent : KontentContract.Intent {
    class LoadTodoListItems : TodoListIntent()
}
