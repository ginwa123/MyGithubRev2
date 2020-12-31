package com.m.ginwa.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m.ginwa.core.domain.model.Following

@Entity(tableName = "followings")
data class FollowingEntity(
    @PrimaryKey(autoGenerate = true)
    val followingId: Long?,

    var loginParent: String?,
    val login: String,
    val avatarUrl: String
) {
    companion object {
        fun mapToDomain(input: FollowingEntity): Following {
            return Following(
                followingId = input.followingId,
                loginParent = input.loginParent,
                login = input.login,
                avatarUrl = input.avatarUrl
            )
        }
    }
}
