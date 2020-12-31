package com.m.ginwa.mygithubrev2.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel : ViewModel() {

    val isExpandToolbar by lazy { MutableLiveData<Map<String, Boolean>>(mapOf()) }
    val progressbarListener by lazy { MutableLiveData(false) }
    val imToolbarListener by lazy { MutableLiveData<String>(null) }
}