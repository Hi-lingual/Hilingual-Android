package com.hilingual.data.user.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PutDeviceInfoRequestDto(
    @SerialName("timezone")
    val timezone: String,
    @SerialName("deviceUuid")
    val deviceUuid: String,
    @SerialName("deviceName")
    val deviceName: String,
    @SerialName("deviceType")
    val deviceType: String,
    @SerialName("osType")
    val osType: String,
    @SerialName("osVersion")
    val osVersion: String,
    @SerialName("appVersion")
    val appVersion: String,
)
