package celcat.logic

import java.text.SimpleDateFormat
import java.util.*

fun parseCelcatDate(date: String): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE).parse(date)
}

fun Date.toCelcatDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE).format(this)

fun Date.toCelcatDateTime(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE).format(this)