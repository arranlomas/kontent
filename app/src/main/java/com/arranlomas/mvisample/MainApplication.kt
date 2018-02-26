package com.arranlomas.mvisample

import com.arranlomas.daggerviewmodelhelper.AppInjector
import com.arranlomas.mvisample.di.AppComponent
import com.arranlomas.mvisample.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MainApplication : DaggerApplication() {

    lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        AppInjector.init(this)
        appComponent = DaggerAppComponent.create()
        appComponent.inject(this)
        return appComponent
    }
}