package com.arranlomas.mvisample.ui.todolist.di

import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 6/02/2018.
 */
@Module
abstract class TodoListBuilder {
    @ContributesAndroidInjector(modules = [
    TodoListModule::class
    ])
    internal abstract fun bindTodoListFragment(): TodoListFragment
}