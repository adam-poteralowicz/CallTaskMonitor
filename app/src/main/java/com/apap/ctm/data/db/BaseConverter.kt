package com.apap.ctm.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParser

open class BaseConverter<T>(private val clazz: Class<T>) {
    val gson = Gson()

    @TypeConverter
    fun toItemFromJson(valueInJson: String?): T = gson.fromJson(valueInJson, clazz)

    @TypeConverter
    fun fromItemToJson(value: T): String = gson.toJson(value)
}

open class BaseListConverter<T>(private val clazz: Class<T>) {
    val gson = Gson()

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