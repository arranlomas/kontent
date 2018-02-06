package com.arranlomas.mvisample.di

import com.arranlomas.mvisample.MainApplication
import com.arranlomas.mvisample.repository.di.RepositoryComponent
import com.arranlomas.mvisample.ui.MainBuilder
import com.arranlomas.mvisample.ui.todolist.di.TodoListBuilder
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
AndroidSupportInjectionModule::class,
TodoListBuilder::class,
MainBuilder::class
], dependencies = [
RepositoryComponent::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: MainApplication)
}