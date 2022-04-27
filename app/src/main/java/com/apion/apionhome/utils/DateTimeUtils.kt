package com.apion.apionhome.utils

import android.R
import android.content.Context
import android.text.format.DateUtils
import com.apion.apionhome.utils.TimeFormat.DATE_FORMAT
import com.apion.apionhome.utils.TimeFormat.TIME_FORMAT_API
import java.text.SimpleDateFormat
import java.util.*


fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun String.toDate(
    dateFormat: String = TIME_FORMAT_API,
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

object TimeFormat {
    const val TIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val TIME_FORMAT_API_1 = "yyyy-MM-dd HH:mm:ss"

    const val HOUR_FORMAT = "HH:mm"
    const val TIME_FORMAT_APP = "EEE, dd MMM HH:mm"
    const val TIME_FORMAT_APP_EXPAND = "EEE, dd MMM yyyy"
    const val DATE_TIME_FORMAT_APP_FULL = "EEE, dd MMM yyyy HH:mm"
    const val DATE_FORMAT = "dd/MM/YYYY"
}

fun Date.getRelativeTimeSpanString(): String? {
    val then: Long
    val fromdate = this
    then = fromdate.time
    val date = Date(then)
    val dateStr = StringBuffer()
    val calendar = Calendar.getInstance()
    calendar.time = date
    val now = Calendar.getInstance()
    val days = daysBetween(calendar.time, now.time)
    val weeks = days / 7
    val minutes = hoursBetween(calendar.time, now.time)
    val hours = minutes / 60
    if (days == 0) {
        val second = minuteBetween(calendar.time, now.time)
        if (minutes > 60) {
            if (hours in 1..24) {
                dateStr.append(
                    java.lang.String.format(
                        "%s %s %s",
                        hours,
                        "giờ",
                        "trước"
                    )
                )
            }
        } else {
            if (second <= 10) {
                dateStr.append("Bây giờ")
            } else if (second > 10 && second <= 30) {
                dateStr.append("Vài giây trước")
            } else if (second > 30 && second <= 60) {
                dateStr.append(
                    java.lang.String.format(
                        "%s %s %s",
                        second,
                        "giây",
                        "trước"
                    )
                )
            } else if (second >= 60 && minutes <= 60) {
                dateStr.append(
                    java.lang.String.format(
                        "%s %s %s",
                        minutes,
                        "phút",
                        "trước"
                    )
                )
            }
        }
    } else if (hours > 24 && days < 7) {
        dateStr.append(
            java.lang.String.format(
                "%s %s %s",
                days,
                "ngày",
                "trước"
            )
        )
    } else if (weeks == 1) {
        dateStr.append(
            java.lang.String.format(
                "%s %s %s",
                weeks,
                "tuần",
                "trước"
            )
        )
    } else {
        return fromdate.formatTo(DATE_FORMAT)
    }
    return dateStr.toString()
}

fun minuteBetween(d1: Date, d2: Date): Int {
    return ((d2.time - d1.time) / DateUtils.SECOND_IN_MILLIS).toInt()
}

fun hoursBetween(d1: Date, d2: Date): Int {
    return ((d2.time - d1.time) / DateUtils.MINUTE_IN_MILLIS).toInt()
}

fun daysBetween(d1: Date, d2: Date): Int {
    return ((d2.time - d1.time) / DateUtils.DAY_IN_MILLIS).toInt()
}
