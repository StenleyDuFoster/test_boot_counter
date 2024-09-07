package com.board.bootcounter.di

import com.board.bootcounter.data.preferences.AppPreferences
import com.board.bootcounter.data.room.CounterDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BootCounterBroadcastReceiverEntryPoint {
    fun provideCounterDao(): CounterDao

    fun provideAppPreferences(): AppPreferences
}