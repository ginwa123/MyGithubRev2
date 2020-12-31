package com.m.ginwa.core.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.m.ginwa.core.data.source.local.entity.FollowingEntity


data class FollowingResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val login: String,
) {
    companion object {
        fun mapToEntity(input: FollowingResponse): FollowingEntity {
            return FollowingEntity(
                followingId = null,
                loginParent = null,
                login = input.login,
                avatarUrl = input.avatarUrl
            )
        }
    }
}