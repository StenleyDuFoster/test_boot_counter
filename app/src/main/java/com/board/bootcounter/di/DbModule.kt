package com.board.bootcounter.di

import android.content.Context
import androidx.room.Room
import com.board.bootcounter.data.room.BootCounterDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideCounterDao(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        BootCounterDb::class.java,
        "boot_counter_db"
    )
        .fallbackToDestructiveMigration()
        .build()
        .dao()
}