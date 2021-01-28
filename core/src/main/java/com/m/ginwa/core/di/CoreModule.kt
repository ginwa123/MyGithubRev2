package com.m.ginwa.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.m.ginwa.core.BuildConfig
import com.m.ginwa.core.data.GithubRepository
import com.m.ginwa.core.data.source.local.LocalDataSource
import com.m.ginwa.core.data.source.local.room.FollowerDao
import com.m.ginwa.core.data.source.local.room.FollowingDao
import com.m.ginwa.core.data.source.local.room.GithubDb
import com.m.ginwa.core.data.source.local.room.UserDao
import com.m.ginwa.core.data.source.remote.RemoteDataSource
import com.m.ginwa.core.data.source.remote.network.RetrofitEndpoint
import com.m.ginwa.core.domain.usecase.GithubInteractor
import com.m.ginwa.core.domain.usecase.GithubUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    private const val BASE_URL = "https://api.github.com/"

    @Singleton
    @Provides
    fun client(): Retrofit {
        val token = BuildConfig.GITHUB_TOKEN
        val httpBuilder = OkHttpClient.Builder()
        val certificate = CertificatePinner.Builder()
            .add(BASE_URL, "sha256/ORtIOYkm5k6Nf2tgAK/uwftKfNhJB3QS0Hs608SiRmE=")
            .add(BASE_URL, "sha256/k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=")
            .build()
        val client = httpBuilder
            .certificatePinner(certificate)
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization", "token $token")
                    .build()
                it.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun endPoint(retrofit: Retrofit): RetrofitEndpoint =
        retrofit.create(RetrofitEndpoint::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(endpoint: RetrofitEndpoint): RemoteDataSource =
        RemoteDataSource(endpoint)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        userDao: UserDao,
        followerDao: FollowerDao,
        followingDao: FollowingDao
    ): LocalDataSource = LocalDataSource(userDao, followerDao, followingDao)

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): GithubRepository =
        GithubRepository(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun provideGithubUseCase(repository: GithubRepository): GithubUseCase =
        GithubInteractor(repository)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): GithubDb {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("todo_secret".toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(context, GithubDb::class.java, "github.db")
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(githubDb: GithubDb): UserDao = githubDb.userDao

    @Provides
    @Singleton
    fun provideFollowingDao(githubDb: GithubDb): FollowingDao = githubDb.followingDao

    @Provides
    @Singleton
    fun provideFollowerDao(githubDb: GithubDb): FollowerDao = githubDb.followerDao


    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}

