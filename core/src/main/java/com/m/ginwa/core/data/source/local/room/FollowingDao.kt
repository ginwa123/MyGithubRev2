package com.m.ginwa.core.data.source.local.room


import androidx.room.*
import com.m.ginwa.core.data.source.local.entity.FollowingEntity
import com.m.ginwa.core.domain.model.Following
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FollowingDao {

    @Transaction
    open suspend fun insertFollowings(loginParent: String, followings: List<FollowingEntity>) {
        deleteFollowings(loginParent)
        insertFollowings(followings)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFollowings(followings: List<FollowingEntity>)

    @Query("SELECT * FROM followings WHERE loginParent=:loginParent")
    abstract fun getFollowings(loginParent: String): Flow<List<Following>>


    @Query("DELETE FROM followings WHERE loginParent=:loginParent")
    abstract suspend fun deleteFollowings(loginParent: String)
}