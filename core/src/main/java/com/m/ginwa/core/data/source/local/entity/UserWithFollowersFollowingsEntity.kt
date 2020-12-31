package com.m.ginwa.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.DataMapper

data class UserWithFollowersFollowingsEntity(
    @Embedded
    val userEntity: UserEntity,

    @Relation(
        parentColumn = "login",
        entityColumn = "loginParent"
    )
    val followers: List<FollowerEntity>,

    @Relation(
        parentColumn = "login",
        entityColumn = "loginParent"
    )
    val followings: List<FollowingEntity>

) {
    companion object {
        fun mapToDomain(input: UserWithFollowersFollowingsEntity?): User {
            return User(
                avatarUrl = input?.userEntity?.avatarUrl,
                company = input?.userEntity?.company,
                followers = DataMapper.mapping(
                    input?.followers ?: listOf()
                ) { FollowerEntity.mapToDomain(it) },
                following = DataMapper.mapping(
                    input?.followings ?: listOf()
                ) { FollowingEntity.mapToDomain(it) },
                location = input?.userEntity?.location,
                publicRepos = input?.userEntity?.publicRepos,
                login = input?.userEntity?.login ?: "",
                name = input?.userEntity?.name,
                isFavorite = input?.userEntity?.isFavorite ?: false
            )
        }
    }
}
