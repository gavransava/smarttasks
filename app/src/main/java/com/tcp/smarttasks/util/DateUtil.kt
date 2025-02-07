package com.tcp.smarttasks.util

import android.content.Context
import com.tcp.smarttasks.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


object DateUtil {

    fun localDateUIFormat(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
        return date.format(dateFormatter)
    }

    fun formatLocalDate(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(dateFormatter)
    }

    fun parseDateString(dateString: String?): LocalDate {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(dateString, dateFormatter)
    }

    fun calculateDaysDifference(targetDateString: String?, dueDateString: String?): Long {
        val targetDate = parseDateString(targetDateString)
        val dueDate = parseDateString(dueDateString)

        require(!targetDate.isAfter(dueDate)) { "targetDate must be earlier than dueDate" }
        return ChronoUnit.DAYS.between(targetDate, dueDate)
    }

    fun formatDueDate(dueDate: String?): String {
        return if (!dueDate.isNullOrEmpty()) {
            localDateUIFormat(parseDateString(dueDate))
        } else {
            "N/A"
        }
    }

    fun calculateDaysLeft(dueDate: String?, context: Context): String {
        return try {
            if (!dueDate.isNullOrEmpty()) {
                val daysLeft = calculateDaysDifference(
                    formatLocalDate(LocalDate.now()),
                    dueDate
                )
                "$daysLeft"
            } else {
                "N/A"
            }
        } catch (e: IllegalArgumentException) {
            context.getString(R.string.overdue)
        }
    }
}