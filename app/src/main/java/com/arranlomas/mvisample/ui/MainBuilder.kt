package com.arranlomas.mvisample.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 6/02/2018.
 */
@Module
abstract class MainBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity
}