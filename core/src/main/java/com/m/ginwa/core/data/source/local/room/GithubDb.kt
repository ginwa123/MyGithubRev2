package com.m.ginwa.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m.ginwa.core.data.source.local.entity.FollowerEntity
import com.m.ginwa.core.data.source.local.entity.FollowingEntity
import com.m.ginwa.core.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, FollowingEntity::class, FollowerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val followingDao: FollowingDao
    abstract val followerDao: FollowerDao
}