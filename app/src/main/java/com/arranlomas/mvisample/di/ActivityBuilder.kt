package com.arranlomas.mvisample.di

import com.arranlomas.mvisample.ui.MainActivity
import com.arranlomas.mvisample.ui.TodoListModule
import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(TodoListModule::class))
    internal abstract fun bindsTodoListFragment(): TodoListFragment

    @ContributesAndroidInjector(modules = arrayOf(TodoListModule::class))
    internal abstract fun bindMainActivity(): MainActivity

}