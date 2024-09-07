package com.board.bootcounter.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.board.bootcounter.data.room.CounterDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ObserveBootDataUseCase @Inject constructor(
    private val counterDao: CounterDao
) {
    companion object {
        private const val TIME_FORMAT = "dd/MM/yyyy"
    }

    private val dateFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    operator fun invoke(): Flow<MainScreenState> =
        counterDao.observeCounters().map {
            groupDatesByDay(it.map { it.time })
        }.map {
            MainScreenState(
                true,
                it.map {
                    BootData(
                        it.key, it.value
                    )
                }.toCollection(SnapshotStateList()))
        }

    fun groupDatesByDay(datesInMillis: List<Long>): Map<String, Int> {
        val datesByDay = mutableMapOf<String, Int>()
        val calendar = Calendar.getInstance()
        val currentTimeZone = calendar.timeZone

        datesInMillis.forEach { dateInMillis ->
            calendar.timeInMillis = dateInMillis
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            val dayOfYear = dateFormatter.format(calendar.timeInMillis)
            calendar.timeZone = currentTimeZone
            datesByDay[dayOfYear] = datesByDay.getOrDefault(dayOfYear, 0) + 1
        }

        return datesByDay
    }
}