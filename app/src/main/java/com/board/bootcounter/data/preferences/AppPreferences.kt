package com.board.bootcounter.data.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferences @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    companion object {
        private const val COUNT_OFF_DISMISS_KEY = "COUNT_OFF_DISMISS_KEY"
    }

    private val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    var countOffDismiss: Int
        get() = preferences.getInt(COUNT_OFF_DISMISS_KEY, 0)

        set(value) {
            preferences.edit {
                putInt(COUNT_OFF_DISMISS_KEY, value)
            }
        }
}