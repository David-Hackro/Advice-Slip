package com.david.hackro.adviceslip.data.data

import androidx.room.Database
import androidx.room.RoomDatabase

private const val DATABASE_VERSION = 1

@Database(
    version = DATABASE_VERSION,
    entities = [AdviceEntity::class]
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun adviceDao(): AdviceDao
}