package com.board.bootcounter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.board.bootcounter.data.room.CounterDao
import com.board.bootcounter.data.room.CounterEntity
import com.board.bootcounter.di.BootCounterBroadcastReceiverEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootBroadcastReceiverTag"
    }

    @Inject
    lateinit var counterDao: CounterDao

    private val scope = CoroutineScope(
        Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.v(TAG, (throwable.message ?: throwable.toString()))
        }
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (!this::counterDao.isInitialized) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                BootCounterBroadcastReceiverEntryPoint::class.java
            )
            counterDao = hiltEntryPoint.provideCounterDao()
        }
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            scope.launch {
                counterDao.insert(
                    CounterEntity(
                        time = Calendar.getInstance(
                            TimeZone.getTimeZone("UTC")
                        ).timeInMillis
                    )
                )
            }
        }
    }
}