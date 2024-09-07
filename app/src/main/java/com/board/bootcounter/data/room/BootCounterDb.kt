package com.board.bootcounter.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CounterEntity::class],
    version = 1
)
abstract class BootCounterDb : RoomDatabase() {
    abstract fun dao(): CounterDao
}