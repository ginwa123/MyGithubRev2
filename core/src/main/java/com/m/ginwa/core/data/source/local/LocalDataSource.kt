package com.m.ginwa.core.data.source.local


import com.m.ginwa.core.data.source.local.entity.FollowerEntity
import com.m.ginwa.core.data.source.local.entity.FollowingEntity
import com.m.ginwa.core.data.source.local.entity.UserEntity
import com.m.ginwa.core.data.source.local.entity.UserWithFollowersFollowingsEntity
import com.m.ginwa.core.data.source.local.room.FollowerDao
import com.m.ginwa.core.data.source.local.room.FollowingDao
import com.m.ginwa.core.data.source.local.room.UserDao
import com.m.ginwa.core.domain.model.Follower
import com.m.ginwa.core.domain.model.Following
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class LocalDataSource(
    private val userDao: UserDao,
    private val followerDao: FollowerDao,
    private val followingDao: FollowingDao
) {
    fun getUsers(login: String): Flow<List<UserEntity>> {
        val loginLike = "%$login%"
        return userDao.getUsers(loginLike).flowOn(Dispatchers.IO)
    }

    fun getUsers(isFavorite: Boolean = true): Flow<List<UserWithFollowersFollowingsEntity>> {
        return userDao.getUsers(isFavorite)
    }

    fun getUser(login: String): Flow<UserWithFollowersFollowingsEntity?> =
        userDao.getUser(login).flowOn(Dispatchers.IO)

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun insertFollowings(loginParent: String,followings: List<FollowingEntity>) =
        followingDao.insertFollowings(loginParent, followings)

    suspend fun insertFollowers(loginParent: String, followers: List<FollowerEntity>) =
        followerDao.insertFollowers(loginParent,followers)

    fun getFollowers(loginParent: String): Flow<List<Follower>> =
        followerDao.getFollowers(loginParent).flowOn(Dispatchers.IO)

    fun getFollowings(loginParent: String): Flow<List<Following>> =
        followingDao.getFollowings(loginParent).flowOn(Dispatchers.Main)


}
