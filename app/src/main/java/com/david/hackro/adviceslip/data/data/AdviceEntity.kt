package com.david.hackro.adviceslip.data.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class AdviceEntity {
    @PrimaryKey
    var id: Int? = null

    @ColumnInfo
    var advice: String? = null

}