package com.m.ginwa.core.domain.usecase

import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Follower
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GithubUseCase {

    suspend fun getUsers(login: String): Flow<Result<List<User>>>
    suspend fun getUser(login: String): Flow<Result<User>>
    suspend fun getFollowers(loginParent: String): Flow<Result<List<Follower>>>
    suspend fun getFollowings(loginParent: String): Flow<Result<List<Following>>>
    suspend fun insertOrUpdateUser(user: User)
    suspend fun getUsersFavorite(isFavorite: Boolean): Flow<List<User>>

}