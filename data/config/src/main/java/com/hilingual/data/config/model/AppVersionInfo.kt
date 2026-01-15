package com.hilingual.data.config.model

data class AppVersionInfo(
    private val minForceVersion: AppVersion,
    private val latestVersion: AppVersion
) {
    fun checkUpdateStatus(currentVersion: AppVersion): UpdateState = when {
        currentVersion < minForceVersion -> UpdateState.FORCE
        currentVersion < latestVersion -> UpdateState.OPTIONAL
        else -> UpdateState.NONE
    }
}
