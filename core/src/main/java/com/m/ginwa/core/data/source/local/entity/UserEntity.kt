package com.m.ginwa.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val login: String,

    val avatarUrl: String?,
    val company: String?,
    val followers: Int?,
    val following: Int?,
    val location: String?,
    val publicRepos: Int?,
    val name: String?,
    var isFavorite: Boolean?
)