package com.m.ginwa.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val useCase: GithubUseCase) : ViewModel() {
    private var _users = MutableLiveData<List<User>>()

    val dataSets: ArrayList<User> by lazy { ArrayList(arrayListOf()) }
    val users: LiveData<List<User>>
        get() = _users

    init {
        viewModelScope.launch {
            getUsersFavorite(true)
        }
    }

    private suspend fun getUsersFavorite(isFavorite: Boolean) {
        useCase.getUsersFavorite(isFavorite).collectLatest { _users.value = it }
    }

    suspend fun updateUser(user: User) {
        useCase.insertOrUpdateUser(user)
    }
}