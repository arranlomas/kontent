package com.arranlomas.mvisample.ui

import com.arranlomas.mvisample.repository.ListItemRepository
import com.arranlomas.mvisample.ui.todolist.TodoListContract
import com.arranlomas.mvisample.ui.todolist.TodoListInteractor
import dagger.Binds
import dagger.Module
import dagger.Provides

//
//@Module
//abstract class MainActivityModule {
//
//    @Binds
//    internal abstract fun provideMainView(mainActivity: MainActivity): MainActivity
//
//    companion object {
//
//        @Provides
//        internal fun providesInteractor(): TodoListContract.Interactor {
//            return TodoListInteractor(ListItemRepository())
//        }
//    }
//}

@Module(includes = arrayOf(MainModule.MainActivtyBinding::class))
class MainModule {

    @Provides
    internal fun providesInteractor(): TodoListContract.Interactor {
        return TodoListInteractor(ListItemRepository())
    }

    @Module
    interface MainActivtyBinding {
        @Binds
        fun bindsMainActivity(mainActivity: MainActivity): MainActivity
    }
}