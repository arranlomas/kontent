package com.arranlomas.mvisample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arranlomas.mvisample.list.TodoListFragment
import com.arranlomas.mvisample.utils.showFragment

/**
 * Created by arran on 21/12/2017.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        showFragment(TodoListFragment(), R.id.mainContent)
    }
}