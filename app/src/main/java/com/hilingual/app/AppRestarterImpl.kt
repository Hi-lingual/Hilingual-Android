/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.app

import android.content.Context
import android.content.Intent
import android.os.Process
import com.hilingual.core.common.app.AppRestarter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.system.exitProcess

class AppRestarterImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppRestarter {

    override fun restartApp() {
        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            } ?: return

        context.startActivity(intent)
        killCurrentProcess()
    }

    private fun killCurrentProcess() {
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}
