package com.m.ginwa.setting.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.core.utils.Constants.THEME
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
            preferenceManager.getString(THEME, getString(R.string.system_default))
        setThemeListener()
    }

    private fun setThemeListener() {
        dropDownPreference.setOnPreferenceChangeListener { preference, newValue ->
            preferenceManager.edit()?.putString(THEME, newValue.toString())?.apply()
            preference.summary = newValue.toString()
            setTheme()
            true
        }
    }

    private fun setTheme() {
        when (preferenceManager.getString(THEME, null)) {
            null, "System Default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }


}