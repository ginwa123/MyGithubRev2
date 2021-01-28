package com.m.ginwa.mygithubrev2.ui.detailuser.followers

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Follower
import com.m.ginwa.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class FollowersViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _followers = MutableLiveData<Result<List<Follower>>>()
    val followersDataSets: ArrayList<Follower> by lazy { ArrayList<Follower>(arrayListOf()) }
    val followers: LiveData<Result<List<Follower>>>
        get() = _followers
    var loginParent: String = ""

    fun getFollowers(loginParent: String) {
        viewModelScope.launch {
            useCase.getFollowers(loginParent).collectLatest { _followers.value = it }
        }
    }
}
