package com.m.ginwa.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m.ginwa.core.domain.model.Follower

@Entity(tableName = "followers")
data class FollowerEntity(
    @PrimaryKey(autoGenerate = true)
    val followerId: Long?,

    var loginParent: String?,
    val login: String,
    val avatarUrl: String
) {
    companion object {
        fun mapToDomain(input: FollowerEntity): Follower {
            return Follower(
                followerId = input.followerId,
                loginParent = input.loginParent,
                login = input.login,
                avatarUrl = input.avatarUrl
            )
        }
    }
}
