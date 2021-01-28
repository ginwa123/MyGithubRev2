package com.m.ginwa.mygithubrev2.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class SearchUsersViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _users = MutableLiveData<Result<List<User>>>()
    val users: LiveData<Result<List<User>>>
        get() = _users
    val dataSets: ArrayList<User> by lazy { ArrayList<User>(listOf()) }


    fun getUsers(login: String) {
        viewModelScope.launch {
            useCase.getUsers(login).collectLatest {
                _users.value = it
            }
        }
    }
}