package pl.wsei.pam.lab06.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    @TypeConverter
    fun fromDateTime(date: LocalDate): String = date.format(formatter)

    @TypeConverter
    fun fromDateTime(str: String): LocalDate = LocalDate.parse(str, formatter)
}
