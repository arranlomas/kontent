package com.arranlomas.mvisample.di

import com.arranlomas.mvisample.MainApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        MainActivityModule::class,
        RepositoryModule::class,
        ViewModelModule::class))
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: MainApplication)
}
