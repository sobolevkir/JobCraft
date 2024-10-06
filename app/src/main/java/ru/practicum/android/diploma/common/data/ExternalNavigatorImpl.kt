package ru.practicum.android.diploma.common.data

import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.common.domain.ExternalNavigator

class ExternalNavigatorImpl(private val appContext: Context) : ExternalNavigator {
    override fun shareText(text: String) {
        Intent(Intent.ACTION_SEND).run {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            startIntent(this)
        }
    }

    private fun startIntent(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}
