package com.m.ginwa.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Follower(
    val followerId: Long?,

    val loginParent: String?,
    val login: String?,
    val avatarUrl: String
) : Parcelable