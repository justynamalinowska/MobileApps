package pl.wsei.pam.lab06.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    companion object {
        const val pattern = "yyyy-MM-dd"

        fun fromMillis(millis: Long): LocalDate =
            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()

        fun toMillis(date: LocalDate): Long =
            date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    @TypeConverter
    fun fromDateTime(date: LocalDate): String =
        date.format(DateTimeFormatter.ofPattern(pattern))

    @TypeConverter
    fun fromDateTime(str: String): LocalDate =
        LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern))
}
