package com.board.bootcounter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.board.bootcounter.data.preferences.AppPreferences
import com.board.bootcounter.di.BootCounterBroadcastReceiverEntryPoint
import com.board.bootcounter.worker.NotificationControlWorkManger
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDeleteReceiver : BroadcastReceiver() {
    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onReceive(context: Context, intent: Intent) {
        if (!this::appPreferences.isInitialized) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                BootCounterBroadcastReceiverEntryPoint::class.java
            )
            appPreferences = hiltEntryPoint.provideAppPreferences()
        }
        val countOffDismiss =  appPreferences.countOffDismiss +1
        if (countOffDismiss == 5) {
            appPreferences.countOffDismiss = 0
            NotificationControlWorkManger.scheduleOneTimeWork(context)
        } else {
            appPreferences.countOffDismiss = countOffDismiss
            NotificationControlWorkManger.scheduleOneTimeWork(context, countOffDismiss * 20L)
        }
    }
}