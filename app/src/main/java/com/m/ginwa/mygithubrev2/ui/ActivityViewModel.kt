package com.m.ginwa.mygithubrev2.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.launch

class ActivityViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val btFavoriteListener by lazy { MutableLiveData<User>(null) }
    val isExpandToolbarListener by lazy { MutableLiveData<Map<String, Boolean>>(mapOf()) }
    val progressbarListener by lazy { MutableLiveData(false) }
    val imToolbarListener by lazy { MutableLiveData<String>(null) }


    fun updateUser(user: User) {
        viewModelScope.launch {
            user.isFavorite = !user.isFavorite
            if (user.login.isNotEmpty()) useCase.insertOrUpdateUser(user)
        }
    }
}