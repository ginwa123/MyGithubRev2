package com.m.ginwa.core.data


import com.m.ginwa.core.data.source.local.LocalDataSource
import com.m.ginwa.core.data.source.local.entity.UserWithFollowersFollowingsEntity
import com.m.ginwa.core.data.source.remote.RemoteDataSource
import com.m.ginwa.core.data.source.remote.network.ApiResponse
import com.m.ginwa.core.data.source.remote.response.FollowerResponse
import com.m.ginwa.core.data.source.remote.response.FollowingResponse
import com.m.ginwa.core.data.source.remote.response.ListUserResponse
import com.m.ginwa.core.data.source.remote.response.UserResponse
import com.m.ginwa.core.domain.model.Follower
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.domain.repository.IGithubRepository
import com.m.ginwa.core.utils.DataMapper
import kotlinx.coroutines.flow.*


class GithubRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IGithubRepository {


    override suspend fun getUsers(login: String): Flow<Result<List<User>>> {
        return remoteDataSource.getUsers(login)
            .map {
                when (it) {
                    is ApiResponse.Success -> {
                        val userEntities = DataMapper.mapping(it.data.items) { userResponse ->
                            ListUserResponse.mapToEntity(userResponse)
                        }
                        Result.Success(userEntities)
                    }
                    is ApiResponse.Error -> Result.Error(it.exception)
                    ApiResponse.Empty -> Result.Success(listOf())
                }
            }
            .onStart { emit(Result.Loading) }
            .catch { emit(Result.Error(Exception(it))) }
    }

    override suspend fun getUser(login: String): Flow<Result<User>> {
        return object : NetworkBoundResult<User, UserResponse>() {
            override suspend fun loadFromDB(): Flow<User> {
                return localDataSource.getUser(login).map {
                    UserWithFollowersFollowingsEntity.mapToDomain(it)
                }
            }

            override suspend fun shouldFetch(data: User?): Boolean = true

            override suspend fun fetchFromNetwork(): ApiResponse<UserResponse> =
                remoteDataSource.getUser(login).single()

            override suspend fun saveFetchResult(dataRemote: UserResponse, dataLocal: User?) {
                val userRemote = UserResponse.mapToEntity(dataRemote)
                if (dataLocal != null) {
                    val userLocal = User.mapToEntity(dataLocal)
                    if (userLocal.isFavorite != null) {
                        userRemote.isFavorite = userLocal.isFavorite
                    }
                }
                localDataSource.insertUser(userRemote)
            }
        }.asFlow()
    }

    override suspend fun getFollowings(loginParent: String): Flow<Result<List<Following>>> {
        return object : NetworkBoundResult<List<Following>, List<FollowingResponse>>() {
            override suspend fun loadFromDB(): Flow<List<Following>> {
                return localDataSource.getFollowings(loginParent)
            }

            override suspend fun shouldFetch(data: List<Following>?): Boolean = true

            override suspend fun fetchFromNetwork(): ApiResponse<List<FollowingResponse>> {
                return remoteDataSource.getFollowings(loginParent).single()
            }

            override suspend fun saveFetchResult(
                dataRemote: List<FollowingResponse>,
                dataLocal: List<Following>?
            ) {
                val followingsRemote = DataMapper.mapping(dataRemote) {
                    FollowingResponse.mapToEntity(it)
                }
                followingsRemote.forEach { it.loginParent = loginParent }
                localDataSource.insertFollowings(loginParent, followingsRemote)
            }
        }.asFlow()
    }

    override suspend fun getFollowers(loginParent: String): Flow<Result<List<Follower>>> {
        return object : NetworkBoundResult<List<Follower>, List<FollowerResponse>>() {
            override suspend fun loadFromDB(): Flow<List<Follower>> {
                return localDataSource.getFollowers(loginParent)
            }

            override suspend fun shouldFetch(data: List<Follower>?): Boolean = true

            override suspend fun fetchFromNetwork(): ApiResponse<List<FollowerResponse>> {
                return remoteDataSource.getFollowers(loginParent).single()
            }

            override suspend fun saveFetchResult(
                dataRemote: List<FollowerResponse>,
                dataLocal: List<Follower>?
            ) {
                val followersRemote = DataMapper.mapping(dataRemote) {
                    FollowerResponse.mapToEntity(it)
                }
                followersRemote.forEach { it.loginParent = loginParent }
                localDataSource.insertFollowers(loginParent, followersRemote)
            }
        }.asFlow()
    }

    override suspend fun insertOrUpdateUser(user: User) {
        val userEntity = User.mapToEntity(user)
        localDataSource.insertUser(userEntity)
    }

    override suspend fun getUsersFavorite(isFavorite: Boolean): Flow<List<User>> {
        return localDataSource.getUsers(isFavorite).map {
            UserWithFollowersFollowingsEntity.mapToDomains(it)
        }
    }

}

