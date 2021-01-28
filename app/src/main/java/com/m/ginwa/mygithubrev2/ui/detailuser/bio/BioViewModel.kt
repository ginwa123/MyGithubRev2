package com.m.ginwa.mygithubrev2.ui.detailuser.bio

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BioViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _user = MutableLiveData<Result<User>>()
    var userData: User? = null
    val user: LiveData<Result<User>>
        get() = _user
    var login: String = ""


    fun getUser(login: String) {
        viewModelScope.launch {
            useCase.getUser(login).collectLatest { _user.value = it }
        }
    }


}