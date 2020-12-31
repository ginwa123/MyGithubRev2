package com.m.ginwa.mygithubrev2.ui.searchfragment

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase
import java.util.ArrayList

class SearchUsersViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var users: LiveData<Result<List<User>>>? = null
    val dataSets: ArrayList<User> by lazy { ArrayList<User>(listOf()) }


    suspend fun getUsers(login: String): LiveData<Result<List<User>>> {
        users = useCase.getUsers(login).asLiveData()
        return users as LiveData<Result<List<User>>>
    }
}