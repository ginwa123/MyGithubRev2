package com.m.ginwa.core.data.source.remote.network


import com.m.ginwa.core.data.source.remote.response.FollowerResponse
import com.m.ginwa.core.data.source.remote.response.FollowingResponse
import com.m.ginwa.core.data.source.remote.response.ListUserResponse
import com.m.ginwa.core.data.source.remote.response.UserResponse
import com.m.ginwa.core.utils.Constants.token
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query



interface RetrofitEndpoint {

    @Headers("Authorization: token $token")
    @GET("search/users")
    suspend fun getUsers(@Query("q") login: String): ListUserResponse

    @Headers("Authorization: token $token")
    @GET("users/{username}")
    suspend fun getUser(@Path("username") login: String): UserResponse


    @Headers("Authorization: token $token")
    @GET("users/{username}/followers")
    suspend fun getFollowersUser(
        @Path("username") login: String,
    ): List<FollowerResponse>

    @Headers("Authorization: token $token")
    @GET("users/{username}/following")
    suspend fun getFollowingsUser(
        @Path("username") login: String,
    ): List<FollowingResponse>

}