package com.m.ginwa.core.data.source.remote.response

import com.google.gson.annotations.SerializedName
import com.m.ginwa.core.data.source.local.entity.UserEntity

data class UserResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("followers")
    val followers: Int?,
    @SerializedName("following")
    val following: Int?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("public_repos")
    val publicRepos: Int?,
    @SerializedName("login")
    val login: String,
    @SerializedName("name")
    val name: String?
) {
    companion object {
        fun mapToEntity(input: UserResponse): UserEntity {
            return UserEntity(
                avatarUrl = input.avatarUrl,
                company = input.company,
                followers = input.followers,
                following = input.following,
                location = input.location,
                publicRepos = input.publicRepos,
                login = input.login,
                name = input.name,
                isFavorite = false
            )
        }

    }
}