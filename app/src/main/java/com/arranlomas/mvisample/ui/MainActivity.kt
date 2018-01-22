package com.arranlomas.mvisample.ui

import android.os.Bundle
import android.util.Log
import com.arranlomas.mvisample.R
import com.arranlomas.mvisample.ui.todolist.TodoListContract
import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import com.arranlomas.mvisample.utils.showFragment
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var interactor: TodoListContract.Interactor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frag = TodoListFragment()
        showFragment(frag, R.id.mainContent)
        Log.v("interactor", interactor.toString())
    }
}