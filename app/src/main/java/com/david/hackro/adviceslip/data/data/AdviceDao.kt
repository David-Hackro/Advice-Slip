package com.david.hackro.adviceslip.data.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AdviceDao {

    @Upsert
    suspend fun updateOrInsertAdvice(advice: AdviceEntity)

    @Query("SELECT * FROM AdviceEntity ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomAdvice() : AdviceEntity
}