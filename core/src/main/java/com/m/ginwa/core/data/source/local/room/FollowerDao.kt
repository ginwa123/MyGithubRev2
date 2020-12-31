package com.m.ginwa.core.data.source.local.room

import androidx.room.*
import com.m.ginwa.core.data.source.local.entity.FollowerEntity
import com.m.ginwa.core.domain.model.Follower
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FollowerDao {

    @Transaction
    open suspend fun insertFollowers(loginParent: String, followers: List<FollowerEntity>) {
        deleteFollowers(loginParent)
        insertFollowers(followers)
    }

    @Query("DELETE FROM followers WHERE loginParent=:loginParent")
    abstract suspend fun deleteFollowers(loginParent: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFollowers(followers: List<FollowerEntity>)

    @Query("SELECT * FROM followers WHERE loginParent=:loginParent")
    abstract fun getFollowers(loginParent: String): Flow<List<Follower>>
}