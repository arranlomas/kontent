package com.arranlomas.mvisample.di

import com.arranlomas.mvisample.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity
}
