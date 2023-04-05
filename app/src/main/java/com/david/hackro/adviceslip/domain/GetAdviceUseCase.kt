package com.david.hackro.adviceslip.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAdviceUseCase  @Inject constructor(private val repository: AdviceRepository) {

    fun invoke(): Flow<Result<Advice>> {
        return repository.getAdvice()
    }
}