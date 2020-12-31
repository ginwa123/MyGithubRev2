package com.m.ginwa.core.di

import android.content.SharedPreferences
import com.m.ginwa.core.data.GithubRepository
import com.m.ginwa.core.data.source.local.LocalDataSource
import com.m.ginwa.core.data.source.local.room.FollowerDao
import com.m.ginwa.core.data.source.local.room.FollowingDao
import com.m.ginwa.core.data.source.local.room.GithubDb
import com.m.ginwa.core.data.source.local.room.UserDao
import com.m.ginwa.core.data.source.remote.RemoteDataSource
import com.m.ginwa.core.data.source.remote.network.RetrofitEndpoint
import com.m.ginwa.core.domain.usecase.GithubUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


// https://developer.android.com/training/dependency-injection/hilt-multi-module
@EntryPoint
@InstallIn(SingletonComponent::class)
interface CoreModuleDependencies {
    fun retrofit(): Retrofit
    fun endpoint(): RetrofitEndpoint
    fun remoteDataSource(): RemoteDataSource
    fun localDataSource(): LocalDataSource
    fun repository(): GithubRepository
    fun useCase(): GithubUseCase
    fun db(): GithubDb
    fun userDao(): UserDao
    fun followingDao(): FollowingDao
    fun followerDao(): FollowerDao
    fun preferenceManager(): SharedPreferences
}