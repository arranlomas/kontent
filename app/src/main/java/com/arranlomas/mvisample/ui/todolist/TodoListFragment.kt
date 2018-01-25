package com.arranlomas.mvisample.ui.todolist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.arranlomas.daggerkontent.DaggerKontentFragment
import com.arranlomas.mvisample.R
import com.arranlomas.mvisample.models.TodoItemState
import com.arranlomas.mvisample.ui.todolist.objects.TodoListIntent
import com.arranlomas.mvisample.ui.todolist.objects.TodoListViewState
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

class TodoListFragment : DaggerKontentFragment<TodoListIntent, TodoListViewState>() {

    @Inject
    lateinit var interactor: TodoListContract.Interactor

    private val adapter = TodoListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setup(interactor, { it.printStackTrace() })
        super.attachIntents(intents())

        todoRecyclerView.hasFixedSize()
        todoRecyclerView.layoutManager = LinearLayoutManager(activity)
        todoRecyclerView.adapter = adapter
    }

    private fun intents(): Observable<TodoListIntent> {
        return Observable.merge(
                initialIntent(),
                adapterIntent(),
                addTodoItemIntent())
    }

    private fun initialIntent(): Observable<TodoListIntent> = Observable.just(TodoListIntent.LoadTodoListItems())

    private fun adapterIntent(): Observable<TodoListIntent> = Observable.create { emitter ->
        adapter.onItemSelected = { serverId, selected ->
            val state = when (selected) {
                true -> TodoItemState.COMPLETED
                false -> TodoItemState.ACTIVE
            }
            emitter.onNext(TodoListIntent.ChangeItemStatus(serverId, state))
        }
    }

    private fun addTodoItemIntent(): Observable<TodoListIntent> =
            RxView.clicks(addTodoFab).flatMap {
                Observable.create<TodoListIntent> { emitter ->
                    context?.let {
                        MaterialDialog.Builder(it)
                                .title("Add Item")
                                .content("Enter todo item details here")
                                .negativeText("cancel")
                                .positiveText("Add")
                                .onPositive { _, _ ->
                                    emitter.onNext(TodoListIntent.AddItem("test title", "test description"))
                                }
                                .show()
                    }
                }
            }

    override fun render(state: TodoListViewState) {
        adapter.items = state.items
        adapter.notifyDataSetChanged()
        state.error?.printStackTrace()
    }
}
