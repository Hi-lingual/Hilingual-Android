package com.hilingual.data.auth.datasource

interface SystemDataSource {
    fun getDeviceName(): String

    fun getDeviceType(): String

    fun getOsVersion(): String

    fun getAppVersion(): String

    fun getProvider(): String

    fun getRole(): String

    fun getOsType(): String
}
