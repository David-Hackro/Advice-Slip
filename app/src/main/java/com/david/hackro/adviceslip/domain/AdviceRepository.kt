package com.david.hackro.adviceslip.domain

import kotlinx.coroutines.flow.Flow

interface AdviceRepository {

    fun getAdvice(): Flow<Result<Advice>>
}