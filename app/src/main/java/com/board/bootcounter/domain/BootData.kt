package com.board.bootcounter.domain

import androidx.compose.runtime.Stable

@Stable
data class BootData(
    val date: String,
    val bootCount: Int,
)