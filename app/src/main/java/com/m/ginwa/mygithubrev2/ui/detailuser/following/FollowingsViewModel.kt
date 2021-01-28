package com.m.ginwa.mygithubrev2.ui.detailuser.following

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class FollowingsViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val followingsDataSets: ArrayList<Following> by lazy { arrayListOf() }
    private var _followingsDataSets = MutableLiveData<Result<List<Following>>>()
    val followings: LiveData<Result<List<Following>>>
        get() = _followingsDataSets
    var login: String = ""

    fun getFollowings(loginParent: String) {
        viewModelScope.launch {
            useCase.getFollowings(loginParent).collectLatest { _followingsDataSets.value = it }
        }
    }
}