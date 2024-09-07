package com.board.bootcounter.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.board.bootcounter.domain.MainScreenState
import com.board.bootcounter.domain.ObserveBootDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeBootDataUseCase: ObserveBootDataUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModelTag"
    }

    private val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.v(TAG, throwable.message ?: throwable.toString())
        }

    private val _bootData = MutableSharedFlow<MainScreenState>(1, 1, BufferOverflow.DROP_OLDEST)
    val bootData = _bootData.asSharedFlow()

    @Volatile
    private var observeDataJob: Job? = null

    fun observeBootData() {
        observeDataJob?.cancel()
        observeDataJob = viewModelScope.launch(coroutineContext) {
            observeBootDataUseCase().collect {
                _bootData.emit(it)
            }
        }
    }
}