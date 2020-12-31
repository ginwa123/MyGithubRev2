package com.m.ginwa.core.domain.usecase


import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.repository.IGithubRepository
import kotlinx.coroutines.flow.Flow

class GithubInteractor(private val iGithubRepository: IGithubRepository): GithubUseCase {
    override suspend fun getUsers(login: String): Flow<Result<List<User>>> =
        iGithubRepository.getUsers(login)

    override suspend fun getUser(login: String): Flow<Result<User>> =
        iGithubRepository.getUser(login)

    override suspend fun getFollowers(loginParent: String) = iGithubRepository.getFollowers(loginParent)

    override suspend fun getFollowings(loginParent: String) = iGithubRepository.getFollowings(loginParent)

    override suspend fun insertOrUpdateUser(user: User) = iGithubRepository.insertOrUpdateUser(user)

    override suspend fun getUsersFavorite(isFavorite: Boolean) = iGithubRepository.getUsersFavorite(isFavorite)
}