package com.david.hackro.adviceslip.data

import com.david.hackro.adviceslip.data.data.AdviceDao
import com.david.hackro.adviceslip.data.remote.AdviceApi
import com.david.hackro.adviceslip.data.remote.ResponseAdvice
import com.david.hackro.adviceslip.data.remote.toEntity
import com.david.hackro.adviceslip.domain.Advice
import com.david.hackro.adviceslip.domain.AdviceRepository
import com.david.hackro.adviceslip.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AdviceRepositoryImpl @Inject constructor(
    private val localSource: AdviceDao,
    private val remoteSource: AdviceApi
) : AdviceRepository {

    override fun getAdvice(): Flow<Result<Advice>> = flow {

        val local = getAdviceFromLocal()

        if (local.isSuccess) {
            emit(local)

            getAdviceFromRemote().onSuccess {
                localSource.updateOrInsertAdvice(it.toEntity())
            }.onFailure {
                emit(Result.failure(it))
            }

        } else {
            getAdviceFromRemote().onSuccess {
                localSource.updateOrInsertAdvice(it.toEntity())
                emit(getAdviceFromLocal())
            }.onFailure {
                emit(Result.failure(it))
            }
        }

    }

    private suspend fun getAdviceFromRemote(): Result<ResponseAdvice> {
        return kotlin.runCatching {
            remoteSource.getAdvice()
        }
    }

    private suspend fun getAdviceFromLocal(): Result<Advice> {
        return kotlin.runCatching {
            localSource.getRandomAdvice().toDomain()
        }
    }
}


