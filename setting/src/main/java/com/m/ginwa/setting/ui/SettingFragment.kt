package com.m.ginwa.setting.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.core.utils.Constants.THEME
import com.m.ginwa.core.utils.DisplayTheme
import com.m.ginwa.core.utils.getEnum
import com.m.ginwa.setting.R
import com.m.ginwa.setting.di.DaggerSettingComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject


class SettingFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var preferenceManager: SharedPreferences
    private lateinit var dropDownPreference: DropDownPreference

    private fun initInject() {
        DaggerSettingComponent.builder()
            .context(requireContext())
            .coreDependencies(
                EntryPointAccessors.fromApplication(
                    requireContext(),
                    CoreModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        initInject()
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        dropDownPreference = findPreference<DropDownPreference>(THEME) as DropDownPreference
        dropDownPreference.summary =
            preferenceManager.getString(THEME, DisplayTheme.DARK.toString())
        setThemeListener()
    }

    private fun setThemeListener() {
        dropDownPreference.setOnPreferenceChangeListener { preference, newValue ->
            preferenceManager.edit()?.putString(THEME, newValue.toString())?.apply()
            preference.summary = newValue.toString()
            setTheme(preferenceManager.getEnum(THEME, newValue.toString()))
            true
        }
    }

    private fun setTheme(displayTheme: DisplayTheme) {
        when (displayTheme) {
            DisplayTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            DisplayTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DisplayTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}