package com.m.ginwa.mygithubrev2.ui.detailuserfragment.followersfragment

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Follower
import com.m.ginwa.core.domain.usecase.GithubUseCase
import java.util.ArrayList

class FollowersViewModel @ViewModelInject constructor(
    private val useCase: GithubUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val followersDataSets: ArrayList<Follower> by lazy { ArrayList<Follower>(arrayListOf()) }
    var followers: LiveData<Result<List<Follower>>>? = null
    suspend fun getFollowers(loginParent: String): LiveData<Result<List<Follower>>>? {
        followers = useCase.getFollowers(loginParent).asLiveData()
        return followers
    }
}
