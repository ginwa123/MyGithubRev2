package com.m.ginwa.mygithubrev2.ui.detailuserfragment.biofragment

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase

class BioViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    var userData: User? = null
    var user: LiveData<Result<User>>? = null

    suspend fun getUser(login: String): LiveData<Result<User>> {
        user = useCase.getUser(login).asLiveData()
        return user as LiveData<Result<User>>
    }

    suspend fun updateUser(user: User) {
        user.isFavorite = !user.isFavorite
        useCase.insertOrUpdateUser(user)
    }



}