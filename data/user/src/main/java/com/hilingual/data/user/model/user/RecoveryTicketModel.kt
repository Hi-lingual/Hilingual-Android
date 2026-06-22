package com.hilingual.data.user.model.user

import com.hilingual.data.user.dto.response.user.RecoveryTicketResponseDto

data class RecoveryTicketModel(
    val ticketId: Long,
    val targetDate: String,
    val remainingChances: Int,
)

internal fun RecoveryTicketResponseDto.toModel() = RecoveryTicketModel(
    ticketId = this.ticketId,
    targetDate = this.targetDate,
    remainingChances = this.remainingChances,
)
