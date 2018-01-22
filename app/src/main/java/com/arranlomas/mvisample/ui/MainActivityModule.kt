package com.arranlomas.mvisample.ui

import com.arranlomas.mvisample.repository.ListItemRepository
import com.arranlomas.mvisample.ui.todolist.TodoListContract
import com.arranlomas.mvisample.ui.todolist.TodoListFragment
import com.arranlomas.mvisample.ui.todolist.TodoListInteractor
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [(TodoListModule.TodoListFragmentBinding::class), (TodoListModule.MainActivtyBinding::class)])
class TodoListModule {

    @Provides
    internal fun providesInteractor(): TodoListContract.Interactor {
        return TodoListInteractor(ListItemRepository())
    }

    @Module
    interface TodoListFragmentBinding {
        @Binds
        fun bindTodoListFragment(todoListFragment: TodoListFragment): TodoListFragment
    }


    @Module
    interface MainActivtyBinding {
        @Binds
        fun bindsMainActivity(mainActivity: MainActivity): MainActivity
    }
}