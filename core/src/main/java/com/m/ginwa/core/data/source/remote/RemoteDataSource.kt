package com.m.ginwa.core.data.source.remote


import com.m.ginwa.core.data.source.remote.network.ApiResponse
import com.m.ginwa.core.data.source.remote.network.RetrofitEndpoint
import com.m.ginwa.core.data.source.remote.response.FollowerResponse
import com.m.ginwa.core.data.source.remote.response.FollowingResponse
import com.m.ginwa.core.data.source.remote.response.ListUserResponse
import com.m.ginwa.core.data.source.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber

class RemoteDataSource(private val endpoint: RetrofitEndpoint) {


    suspend fun getUsers(login: String): Flow<ApiResponse<ListUserResponse>> {
        return apiCall { endpoint.getUsers(login) }
    }

    suspend fun getUser(login: String): Flow<ApiResponse<UserResponse>> {
        return apiCall { endpoint.getUser(login) }
    }

    suspend fun getFollowers(login: String): Flow<ApiResponse<List<FollowerResponse>>> {
        return apiCall { endpoint.getFollowersUser(login) }
    }

    suspend fun getFollowings(login: String): Flow<ApiResponse<List<FollowingResponse>>> {
        return apiCall { endpoint.getFollowingsUser(login) }
    }

    private suspend fun <T> apiCall(service: suspend () -> T): Flow<ApiResponse<T>> {

        return flow {
            try {
                val result = service.invoke()
                if (result is ListUserResponse) {
                    if (result.items.isEmpty()) emit(ApiResponse.Empty)
                    else emit(ApiResponse.Success(result))
                } else if (result is List<*>) {
                    if (result.isEmpty()) emit(ApiResponse.Empty)
                    else emit(ApiResponse.Success(result))
                } else {
                    emit(ApiResponse.Success(result))
                }
                Timber.d(result.toString())
            } catch (e: HttpException) {
                Timber.e(e)
                emit(ApiResponse.Error(e))
            } catch (e: Exception) {
                Timber.e(e)
                emit(ApiResponse.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

}