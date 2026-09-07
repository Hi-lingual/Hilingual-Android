package com.hilingual.core.work.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hilingual.core.localstorage.datasource.ReminderPreferenceDataSource
import com.hilingual.core.work.scheduler.ReminderScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderPreferenceDataSource: ReminderPreferenceDataSource

    @Inject lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val preference = reminderPreferenceDataSource.reminderFlow.first()
            if (preference.isEnabled) reminderScheduler.schedule(preference)
            pendingResult.finish()
        }
    }
}
