package com.tcp.smarttasks.util

import android.app.AlertDialog
import android.widget.EditText
import android.widget.LinearLayout
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

fun AlertDialog.Builder.showAddCommentDialog(
    listener: (string: String) -> Unit
) {
    val context = this.context
     val inputField = EditText(context)
    inputField.hint = context.getString(R.string.enter_comment)

    val layout = LinearLayout(context)
    layout.orientation = LinearLayout.VERTICAL
    layout.addView(inputField)

    this.setTitle(context.getString(R.string.enter_comment_title))
    this.setCancelable(false)

    this.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
        listener(inputField.text.toString())
    }

    this.setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
        listener("")
        dialog.dismiss()
    }

    this.setView(layout)

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

fun Fragment.showErrorDialog(
    tag: String,
    errorMessage: String?,
    dialogAction: (() -> Unit)? = null
) {
    Timber.d("Error occurred on $tag:\n$errorMessage")
    AlertDialog.Builder(requireContext()).showErrorDialog(
        errorMessage ?: ""
    ) {
        dialogAction?.invoke()
    }
}

fun Fragment.showAddCommentDialog(
    dialogAction: ((string: String) -> Unit)
) {
    AlertDialog.Builder(requireContext()).showAddCommentDialog(dialogAction)
}