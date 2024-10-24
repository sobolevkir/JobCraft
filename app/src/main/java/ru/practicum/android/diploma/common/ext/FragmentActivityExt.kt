package ru.practicum.android.diploma.common.ext

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.hideKeyboard() {
    this.currentFocus?.let { view ->
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
