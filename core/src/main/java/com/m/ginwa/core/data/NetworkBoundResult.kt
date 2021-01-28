package com.m.ginwa.core.data

import com.m.ginwa.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResult<DomainType, ResponseType> {


    private var result: Flow<Result<DomainType>>

    init {
        result = flow {
            val localDb = loadFromDB()
            if (localDb != null) {
                emit(Result.Loading)
                emit(Result.Success(localDb.first()))
                if (shouldFetch(data = localDb.first())) {
                    emit(Result.Loading)
                    val apiResponse = fetchFromNetwork()
                    if (apiResponse != null) {
                        when (apiResponse) {
                            is ApiResponse.Success -> {
                                saveFetchResult(apiResponse.data, localDb.first())
                                emitAll(localDb.map { Result.Success(it) })
                            }
                            is ApiResponse.Error -> {
                                emit(Result.Error(apiResponse.exception))
                                emitAll(localDb.map { Result.Success(it) })
                            }
                            ApiResponse.Empty -> {
                                emitAll(localDb.map { Result.Success(it) })
                            }
                        }
                    } else {
                        emitAll(localDb.map { Result.Success(it) })
                    }
                } else {
                    emitAll(localDb.map { Result.Success(it) })
                }
            }
        }
    }

    protected abstract suspend fun loadFromDB(): Flow<DomainType>?

    protected abstract suspend fun shouldFetch(data: DomainType?): Boolean

    protected abstract suspend fun fetchFromNetwork(): ApiResponse<ResponseType>?

    protected abstract suspend fun saveFetchResult(dataRemote: ResponseType, dataLocal: DomainType?)

    fun asFlow(): Flow<Result<DomainType>> = result
}