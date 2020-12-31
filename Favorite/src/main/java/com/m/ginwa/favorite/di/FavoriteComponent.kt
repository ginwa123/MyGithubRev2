package com.m.ginwa.favorite.di

import android.content.Context
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.favorite.ui.FavoriteFragment
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [CoreModuleDependencies::class])
interface FavoriteComponent {

    fun inject(favoriteFragment: FavoriteFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun coreDependencies(coreModuleDependencies: CoreModuleDependencies): Builder
        fun build(): FavoriteComponent
    }
}