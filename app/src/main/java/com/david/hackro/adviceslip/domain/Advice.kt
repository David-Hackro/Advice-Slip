package com.david.hackro.adviceslip.domain

import com.david.hackro.adviceslip.data.data.AdviceEntity

data class Advice(val advice: String, val id: Int? = null)

fun AdviceEntity.toDomain(): Advice {
    return Advice(advice.orEmpty(), id)
}
