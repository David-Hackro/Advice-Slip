package com.david.hackro.adviceslip.data.remote

import com.david.hackro.adviceslip.data.data.AdviceEntity
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ResponseAdvice(
    @SerialName("slip")
    val slip: Slip? = null
)

@kotlinx.serialization.Serializable
data class Slip(
    @SerialName("advice")
    val advice: String? = null,
    @SerialName("id")
    val id: Int? = null
)


fun ResponseAdvice.toEntity(): AdviceEntity {
    return AdviceEntity().apply {
        id = this@toEntity.slip?.id
        advice = this@toEntity.slip?.advice
    }
}
