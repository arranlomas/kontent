package com.arranlomas.mvisample.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arranlomas.kontent.commons.KontentFragment
import com.arranlomas.mvisample.R
import com.arranlomas.mvisample.list.objects.TodoListIntent
import com.arranlomas.mvisample.list.objects.TodoListViewState
import com.arranlomas.mvisample.repository.ListItemRepository
import io.reactivex.Observable

/**
 * Created by arran on 4/12/2017.
 */
internal class TodoListFragment : KontentFragment<TodoListViewState, TodoListIntent>() {

    var interactor: TodoListContract.Interactor

    init {
        //this step would preferbly be done via dagger ot dependency injects, and the interactor would just be initejected as an interface, this step is vital for testing
        val authRepository = ListItemRepository()
        this.interactor = TodoListInteractor(authRepository)
        super.setup(interactor)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.attachIntents(intents())
    }

    fun intents(): Observable<TodoListIntent> {
        return initialIntent()
    }

    fun initialIntent(): Observable<TodoListIntent> = Observable.just(TodoListIntent.LoadTodoListItems())

    override fun render(state: TodoListViewState) {
        Log.v("RESULTS!", state.items.size.toString())
    }
}
