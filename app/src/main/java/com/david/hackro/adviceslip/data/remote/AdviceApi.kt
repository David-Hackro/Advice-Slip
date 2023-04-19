package com.david.hackro.adviceslip.data.remote

import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    suspend fun getAdvice(): ResponseAdvice
}