
package com.hilingual.app

import android.content.Context
import com.hilingual.core.common.app.AppRestarter
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppRestarterImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppRestarter {
    override fun restartApp() {
        ProcessPhoenix.triggerRebirth(context)
    }
}
