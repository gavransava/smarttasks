package com.tcp.smarttasks.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


object DateUtil {

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

    fun isDateBetween(
        selectedDate: String,
        targetDateString: String,
        dueDateString: String?
    ): Boolean {

        val targetDate = parseDateString(targetDateString)
        val checkDate = parseDateString(selectedDate)

        if (dueDateString == null) {
            return checkDate.isEqual(targetDate) || checkDate.isAfter(targetDate)
        } else {
            val dueDate = parseDateString(dueDateString)

            require(!targetDate.isAfter(dueDate)) { "targetDate must be earlier than dueDate" }

            return checkDate.isEqual(targetDate) || (checkDate.isAfter(targetDate) && checkDate.isBefore(
                dueDate
            )) || checkDate.isEqual(dueDate)
        }
    }
}