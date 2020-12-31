package com.m.ginwa.core.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.m.ginwa.core.domain.model.User

data class ListUserResponse(
    @SerializedName("items")
    val items: List<UserResponse>
) {

    data class UserResponse(
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("login")
        val login: String,
    )

    companion object {
        fun mapToEntity(input: UserResponse): User {
            return User(
                avatarUrl = input.avatarUrl,
                login = input.login,
                company = null,
                location = null,
                publicRepos = null,
                name = null,
                isFavorite = false,
                followers = null,
                following = null
            )
        }
    }
}


