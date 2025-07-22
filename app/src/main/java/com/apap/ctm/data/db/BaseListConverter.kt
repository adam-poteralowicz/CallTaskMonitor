package com.apap.ctm.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParser

open class BaseListConverter<T>(private val clazz: Class<T>) {
    private val gson = Gson()

    @TypeConverter
    fun toItemFromJson(valueInJson: String?): List<T>? = valueInJson?.let {
        JsonParser.parseString(it).asJsonArray.map { jsonElement ->
            gson.fromJson(jsonElement, clazz)
        }
    }

    @TypeConverter
    fun fromItemToJson(value: List<T>?): String? = value?.let {
        gson.toJson(value)
    }
}