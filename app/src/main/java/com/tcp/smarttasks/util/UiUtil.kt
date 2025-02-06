package com.tcp.smarttasks.util

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tcp.smarttasks.R
import timber.log.Timber

fun AlertDialog.Builder.showOKDialog(title: String, message: String, listener: () -> Unit) {
    this.setTitle(title)
    this.setMessage(message)
    this.setCancelable(false)
    this.setPositiveButton(context.getString(R.string.ok)) { _, _ ->
        listener()
    }
    val dialog = this.create()
    dialog.show()
}

fun AlertDialog.Builder.showErrorDialog(errorMessage: String, listener: () -> Unit) {
    this.setTitle(context.getString(R.string.error_occurred))
    this.setMessage(errorMessage)
    this.setCancelable(false)
    this.setPositiveButton(context.getString(R.string.ok)) { _, _ ->
        listener()
    }
    val dialog = this.create()
    dialog.show()
}

fun Fragment.showErrorDialog(tag: String, errorMessage: String?, dialogAction: (() -> Unit)? = null) {
    Timber.d("Error occurred on $tag:\n$errorMessage")
    AlertDialog.Builder(requireContext()).showErrorDialog(
        errorMessage ?: ""
    ) {
        dialogAction?.invoke()
    }
}