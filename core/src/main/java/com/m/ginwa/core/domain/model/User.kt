package com.m.ginwa.core.domain.model

import com.m.ginwa.core.data.source.local.entity.UserEntity


data class User(
    val avatarUrl: String?,
    val company: String?,
    val location: String?,
    val publicRepos: Int?,
    val login: String,
    val name: String?,
    var isFavorite: Boolean,

    val followers: List<Follower>?,
    val following: List<Following>?,
) {
    companion object {
        fun mapToEntity(input: User): UserEntity {
            return UserEntity(
                avatarUrl = input.avatarUrl,
                company = input.company,
                followers = input.followers?.size,
                following = input.following?.size,
                location = input.location,
                publicRepos = input.publicRepos,
                login = input.login,
                name = input.name,
                isFavorite = input.isFavorite
            )
        }
    }
}