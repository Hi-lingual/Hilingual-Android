package com.hilingual.data.auth.datasourceimpl

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.hilingual.data.auth.datasource.SystemDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class SystemDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SystemDataSource {

    override fun getDeviceName(): String = Build.MODEL

    override fun getDeviceType(): String =
        if (context.resources.configuration.smallestScreenWidthDp >= 600) "TABLET" else "PHONE"

    override fun getOsVersion(): String = Build.VERSION.RELEASE

    override fun getAppVersion(): String {
        val packageManager = context.packageManager
        val packageName = context.packageName
        
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        
        return packageInfo.versionName ?: "2.0.0"
    }

    override fun getProvider(): String = "GOOGLE"

    override fun getRole(): String = "USER"

    override fun getOsType(): String = "Android"
}
