package com.arranlomas.mvisample.ui.todolist.di

import com.arranlomas.mvisample.repository.IListItemRepository
import com.arranlomas.mvisample.repository.ListItemRepository
import com.arranlomas.mvisample.ui.todolist.TodoListContract
import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import com.arranlomas.mvisample.ui.todolist.TodoListInteractor
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module()
class TodoListModule {

    @Provides
    internal fun providesInteractor(listItemRepository: IListItemRepository): TodoListContract.Interactor {
        return TodoListInteractor(listItemRepository)
    }
}