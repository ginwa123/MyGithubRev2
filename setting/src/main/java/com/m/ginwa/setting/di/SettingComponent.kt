package com.m.ginwa.setting.di

import android.content.Context
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.setting.ui.SettingFragment
import dagger.BindsInstance
import dagger.Component


@Component(dependencies = [CoreModuleDependencies::class])
interface SettingComponent {

    fun inject(settingFragment: SettingFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun coreDependencies(coreModuleDependencies: CoreModuleDependencies): Builder
        fun build(): SettingComponent
    }
}