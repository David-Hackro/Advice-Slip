package com.david.hackro.adviceslip.di

import com.david.hackro.adviceslip.data.AdviceRepositoryImpl
import com.david.hackro.adviceslip.data.remote.AdviceApi
import com.david.hackro.adviceslip.domain.AdviceRepository
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [AdviceModule.BindRepository::class])
@InstallIn(SingletonComponent::class)
object AdviceModule {

    @Provides
    @Singleton
    fun provideGetAdviceUseCase(repository: AdviceRepository): GetAdviceUseCase {
        return GetAdviceUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAdviceApi(retrofit: Retrofit): AdviceApi {
        return retrofit.create(AdviceApi::class.java)
    }


    @Module
    @InstallIn(SingletonComponent::class)
    interface BindRepository {
        @Binds
        @Singleton
        fun bindRepository(adviceRepository: AdviceRepositoryImpl): AdviceRepository
    }
}