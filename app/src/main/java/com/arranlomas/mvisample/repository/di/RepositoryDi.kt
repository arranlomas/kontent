package com.arranlomas.mvisample.repository.di

import com.arranlomas.mvisample.repository.IListItemRepository
import com.arranlomas.mvisample.repository.ListItemRepository
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 6/02/2018.
 */
@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun getListItemRepository(): IListItemRepository
}


@Module
class RepositoryModule {
    @Provides
    fun providesListItemRepository(): IListItemRepository {
        return ListItemRepository()
    }
}