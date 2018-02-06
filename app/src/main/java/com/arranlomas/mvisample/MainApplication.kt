package com.arranlomas.mvisample

import com.arranlomas.mvisample.di.DaggerAppComponent
import com.arranlomas.mvisample.repository.di.DaggerRepositoryComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MainApplication : DaggerApplication() {

    private val repositoryComponent by lazy {
        DaggerRepositoryComponent.create()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().repositoryComponent(repositoryComponent).build()
        appComponent.inject(this)
        return appComponent
    }
}