package com.board.bootcounter.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.board.bootcounter.data.room.CounterDao
import com.board.bootcounter.data.room.CounterEntity
import com.board.bootcounter.di.BootCounterBroadcastReceiverEntryPoint
import com.board.bootcounter.notification.CustomNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltWorker
class NotificationControlWorkManger @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val TAG = "NotificationControlWorkManger"

        fun scheduleOneTimeWork(context: Context, time: Long = 15) {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG)

            val constraints = Constraints.Builder()
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationControlWorkManger>()
                .setInitialDelay(time, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(TAG)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    @Inject
    lateinit var counterDao: CounterDao

    override suspend fun doWork(): Result {
        if (!this::counterDao.isInitialized) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                appContext.applicationContext,
                BootCounterBroadcastReceiverEntryPoint::class.java
            )
            counterDao = hiltEntryPoint.provideCounterDao()
        }

        CustomNotificationManager(appContext).createNotification(
            formatBootData(
                counterDao.observeCounters().firstOrNull() ?: listOf()
            )
        )
        return Result.success()
    }

    private fun formatBootData(bootEvents: List<CounterEntity>): String {
        return when {
            bootEvents.isEmpty() -> "No boots detected"
            bootEvents.size == 1 -> {
                val date = formatDate(bootEvents[0].time)
                "The boot was detected = $date"
            }

            else -> {
                val lastEvent = bootEvents.last()
                val preLastEvent = bootEvents[bootEvents.size - 2]
                val timeDelta = lastEvent.time - preLastEvent.time
                "Last boots time delta = ${formatTimeDelta(timeDelta)}"
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

    fun formatTimeDelta(timeDelta: Long): String {
        val seconds = timeDelta / 1000
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60)
    }
}