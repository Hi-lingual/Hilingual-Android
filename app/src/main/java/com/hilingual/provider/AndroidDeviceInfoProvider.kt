package com.hilingual.provider

import android.content.Context
import android.os.Build
import com.hilingual.core.common.extension.appVersionName
import com.hilingual.core.common.provider.DeviceInfoProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AndroidDeviceInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceInfoProvider {

    override fun getDeviceName(): String = Build.MODEL

    override fun getDeviceType(): String =
        if (context.resources.configuration.smallestScreenWidthDp >= 600) "TABLET" else "PHONE"

    override fun getOsVersion(): String = Build.VERSION.RELEASE

    override fun getAppVersion(): String = context.appVersionName

    override fun getProvider(): String = "GOOGLE"

    override fun getRole(): String = "USER"

    override fun getOsType(): String = "Android"
}
