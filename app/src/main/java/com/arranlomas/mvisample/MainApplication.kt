package com.arranlomas.mvisample

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import com.arranlomas.mvisample.di.DaggerAppComponent
import com.arranlomas.mvisample.di.AppComponent


class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}