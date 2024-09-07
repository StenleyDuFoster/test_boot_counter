package com.board.bootcounter.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CounterEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Long = 0,
    val time: Long
)