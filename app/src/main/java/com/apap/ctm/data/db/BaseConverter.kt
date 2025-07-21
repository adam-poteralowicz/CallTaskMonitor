package com.apap.ctm.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.joda.time.DateTime
import java.lang.reflect.Type


open class BaseConverter<T>(private val clazz: Class<T>) {
    private val gson = Gson()

    @TypeConverter
    fun toItemFromJson(valueInJson: String?): T = gson.fromJson(valueInJson, clazz)

    @TypeConverter
    fun fromItemToJson(value: T): String = gson.toJson(value)
}

open class BaseListConverter<T>(private val clazz: Class<T>) {
    private val gson = Gson()

    @TypeConverter
    fun toItemFromJson(valueInJson: String?): List<T>? = valueInJson?.let {
        JsonParser.parseString(it).asJsonArray.map {
            gson.fromJson(it, clazz)
        }
    }

    @TypeConverter
    fun fromItemToJson(value: List<T>?): String? = value?.let {
        gson.toJson(value)
    }
}

open class DateTimeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToDate(data: String?): DateTime? {
        val type: Type? = object : TypeToken<DateTime?>() {}.type

        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun dateToString(dateTime: DateTime?): String? {
        return gson.toJson(dateTime)
    }
}