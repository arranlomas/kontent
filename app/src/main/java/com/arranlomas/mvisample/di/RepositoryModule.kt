package com.arranlomas.mvisample.di

import com.arranlomas.mvisample.IScoreRepository
import com.arranlomas.mvisample.ScoreRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun providesScoreRepository(): IScoreRepository {
        return ScoreRepository()
    }
}