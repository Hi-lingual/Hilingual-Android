package com.hilingual.data.user.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecoveryTicketResponseDto(
    @SerialName("ticketId")
    val ticketId: Long,
    @SerialName("targetDate")
    val targetDate: String,
    @SerialName("remainingChances")
    val remainingChances: Int,
)
