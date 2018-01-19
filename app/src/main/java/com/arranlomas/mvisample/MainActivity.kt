package com.arranlomas.mvisample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import com.arranlomas.mvisample.utils.showFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by arran on 21/12/2017.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showFragment(TodoListFragment(), R.id.mainContent)

        addTodoFab.setOnClickListener {

        }
    }
}