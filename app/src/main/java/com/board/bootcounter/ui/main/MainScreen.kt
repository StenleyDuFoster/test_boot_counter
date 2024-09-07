package com.board.bootcounter.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.board.bootcounter.domain.BootData
import com.board.bootcounter.domain.MainScreenState

@Composable
fun MainScreen(
    state: MainScreenState,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = state,
        modifier = modifier
    ) {
        if (it.isDataLoaded) {
            Column(
                Modifier.fillMaxWidth()
            ) {
                state.data.forEach {
                    Text(
                        text = "${it.date} - ${it.bootCount}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    MainScreen(
        MainScreenState(),
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}

@Preview
@Composable
private fun PreviewData() {
    MainScreen(
        MainScreenState(
            true,
            listOf(
                BootData("2023-01-01", 1),
                BootData("2023-01-02", 2),
                BootData("2023-01-03", 3),
            ).toCollection(SnapshotStateList())
        ),
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}