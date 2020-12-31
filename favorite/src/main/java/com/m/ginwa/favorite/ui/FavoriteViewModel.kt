package com.m.ginwa.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val useCase: GithubUseCase): ViewModel() {


    val dataSets: ArrayList<User> by lazy { ArrayList(arrayListOf()) }
    var users: LiveData<Result<List<User>>>? = null

    suspend fun getUsersFavorite(isFavorite: Boolean): LiveData<Result<List<User>>>? {
        users =  useCase.getUsersFavorite(isFavorite).asLiveData()
        return users
    }

    suspend fun updateUser(user: User) {
        useCase.insertOrUpdateUser(user)
    }
}