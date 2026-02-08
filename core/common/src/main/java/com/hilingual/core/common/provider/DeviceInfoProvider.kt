package com.hilingual.core.common.provider

interface DeviceInfoProvider {
    fun getDeviceName(): String

    fun getDeviceType(): String

    fun getOsVersion(): String

    fun getAppVersion(): String

    fun getProvider(): String

    fun getRole(): String

    fun getOsType(): String
}
