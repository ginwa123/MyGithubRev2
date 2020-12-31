package com.m.ginwa.favorite.di

import androidx.lifecycle.ViewModelProvider
import com.m.ginwa.favorite.ui.FavoriteFragment
import com.m.ginwa.favorite.ui.FavoriteViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
object ViewModelModule {

    @Provides
    fun provideFavoriteViewModel(
        fragment: FavoriteFragment,
        factory: ViewModelFactory
    ): FavoriteViewModel {
        return ViewModelProvider(fragment, factory).get(FavoriteViewModel::class.java)
    }

}