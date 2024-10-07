package ru.practicum.android.diploma.vacancy.data.impl

import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

class ExternalNavigatorImpl(private val appContext: Context) : ExternalNavigator {
    override fun shareText(text: String) {
        Intent(Intent.ACTION_SEND).run {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            val chooserIntent = Intent.createChooser(this, appContext.getString(R.string.select_send_service))
            startIntent(chooserIntent)
        }
    }

    private fun startIntent(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}
