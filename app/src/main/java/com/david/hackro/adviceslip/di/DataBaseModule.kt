package com.david.hackro.adviceslip.di

import android.content.Context
import androidx.room.Room
import com.david.hackro.adviceslip.data.data.AdviceDao
import com.david.hackro.adviceslip.data.data.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val APP_DATABASE = "advice_db"

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, APP_DATABASE).build()
    }

    @Singleton
    @Provides
    fun provideAdviceDao(dataBase: AppDataBase): AdviceDao {
        return dataBase.adviceDao()
    }
}