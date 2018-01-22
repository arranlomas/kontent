package com.arranlomas.mvisample.ui

import android.os.Bundle
import com.arranlomas.mvisample.R
import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import com.arranlomas.mvisample.utils.showFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showFragment(TodoListFragment(), R.id.mainContent)
    }
}