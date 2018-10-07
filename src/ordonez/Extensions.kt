package ordonez

import java.text.SimpleDateFormat
import java.util.*

val dateFormat: ThreadLocal<SimpleDateFormat> = ThreadLocal.withInitial {
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
}

operator fun String.times(count: Int) =
        StringBuilder(this.length * count)
                .apply {
                    for (i in 0 until count) {
                        append(this@times)
                    }
                }
                .toString()

fun String.toDate(): Date = dateFormat.get().parse(this)
fun String.toCalendar(): Calendar = Calendar.getInstance().apply {
    timeInMillis = this@toCalendar.toDate().time
}

val Calendar.minuteInDay: Int
    get() = get(Calendar.MINUTE) + get(Calendar.HOUR_OF_DAY) * 60

fun String.toLocation() = Location.valueOf(this)
fun Int.toLocation() = Location.values()[this]

fun String.toSensorType() = SensorType.valueOf(this)
fun Int.toSensorType() = SensorType.values()[this]

fun String.toLabel() = Label.valueOf(this)
fun Int.toLabel() = Label.values()[this]

fun String.toRoom() = Room.valueOf(this)
fun Int.toRoom() = Room.values()[this]

fun LongRange.overlap(other: LongRange): Long =
        if (!contains(other.start) && !contains(other.endInclusive)) {
            0
        } else if (contains(other.start) && contains(other.endInclusive)) {
            other.endInclusive - other.start
        } else if (other.contains(start) && other.contains(endInclusive)) {
            endInclusive - start
        } else if (contains(other.start)) {
            endInclusive - other.start
        } else {
            other.endInclusive - start
        }