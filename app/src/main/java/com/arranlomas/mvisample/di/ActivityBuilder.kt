package com.arranlomas.mvisample.di

import com.arranlomas.mvisample.ui.MainActivity
import com.arranlomas.mvisample.ui.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    internal abstract fun bindMainActivity(): MainActivity

}