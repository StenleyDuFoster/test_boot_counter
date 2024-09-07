package com.board.bootcounter.domain

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Stable
class MainScreenState(
    val isDataLoaded: Boolean = false,
    val data: SnapshotStateList<BootData> = SnapshotStateList(),
)