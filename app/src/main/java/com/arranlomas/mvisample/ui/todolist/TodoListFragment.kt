package com.arranlomas.mvisample.ui.todolist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.arranlomas.kontent.commons.objects.android.KontentFragment
import com.arranlomas.mvisample.R
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState
import com.arranlomas.mvisample.models.TodoItemState
import com.arranlomas.mvisample.repository.ListItemRepository
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Emitter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*

internal class TodoListFragment : KontentFragment<TodoListViewState, TodoListIntent>() {

    var interactor: TodoListContract.Interactor

    val adapter = TodoListAdapter()

    init {
        //this step would preferbly be done via dagger ot dependency injects, and the interactor would just be initejected as an interface, this step is vital for testing
        val authRepository = ListItemRepository()
        this.interactor = TodoListInteractor(authRepository)
        super.setup(interactor, {
            it.printStackTrace()
            throw it
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.attachIntents(intents())

        todoRecyclerView.hasFixedSize()
        todoRecyclerView.layoutManager = LinearLayoutManager(activity)
        todoRecyclerView.adapter = adapter
    }

    fun showAddTodoItemDialog(): Observable<TodoListIntent.AddItem> {
        return Observable.create { emitter ->
            context?.let {
                MaterialDialog.Builder(it)
                        .title("Add Item")
                        .content("Enter todo item details here")
                        .negativeText("cancel")
                        .positiveText("Add")
                        .onPositive { dialog, which ->
                            emitter.onNext(TodoListIntent.AddItem("test title", "test description"))
                        }
                        .show()
            }
        }
    }

    fun intents(): Observable<TodoListIntent> {
        return Observable.merge(
                initialIntent(),
                adapterIntent(),
                addTodoItemIntent())
    }

    fun initialIntent(): Observable<TodoListIntent> = Observable.just(TodoListIntent.LoadTodoListItems())

    fun adapterIntent(): Observable<TodoListIntent> = Observable.create { emitter ->
        adapter.onItemSelected = { serverId, selected ->
            val state = when (selected) {
                true -> TodoItemState.COMPLETED
                false -> TodoItemState.ACTIVE
            }
            emitter.onNext(TodoListIntent.ChangeItemStatus(serverId, state))
        }
    }

    fun addTodoItemIntent(): Observable<TodoListIntent> = RxView.clicks(addTodoFab).flatMap {
        showAddTodoItemDialog()
    }

    override fun render(state: TodoListViewState) {
        adapter.items = state.items
        adapter.notifyDataSetChanged()
        state.error?.printStackTrace()
    }
}
