package com.m.ginwa.core.data.source.local.room

import androidx.room.*
import com.m.ginwa.core.data.source.local.entity.UserEntity
import com.m.ginwa.core.data.source.local.entity.UserWithFollowersFollowingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Query("SELECT * FROM users WHERE login LIKE :login")
    abstract fun getUsers(login: String): Flow<List<UserEntity>>

    @Transaction
    @Query("SELECT * FROM users WHERE isFavorite =:isFavorite ORDER BY login")
    abstract fun getUsers(isFavorite: Boolean): Flow<List<UserWithFollowersFollowingsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addUsers(users: List<UserEntity>)

    @Transaction
    @Query("SELECT * FROM users WHERE login =:login")
    abstract fun getUser(login: String): Flow<UserWithFollowersFollowingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(user: UserEntity)

}