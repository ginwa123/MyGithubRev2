package com.m.ginwa.mygithubrev2.ui.detailuserfragment.followingfragment

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.domain.usecase.GithubUseCase
import java.util.ArrayList

class FollowingsViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val followingsDataSets: ArrayList<Following> by lazy { ArrayList<Following>(arrayListOf()) }
    var followings: LiveData<Result<List<Following>>>? = null


    suspend fun getFollowings(loginParent: String): LiveData<Result<List<Following>>>? {
        followings = useCase.getFollowings(loginParent).asLiveData()
        return followings
    }
}