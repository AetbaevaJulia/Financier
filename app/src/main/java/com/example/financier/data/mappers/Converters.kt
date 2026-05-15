package com.example.financier.data.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringToMap(value: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(value, type) ?: emptyMap()
    }

    @TypeConverter
    fun fromMapToString(map: Map<String, Double>): String {
        return Gson().toJson(map)
    }
}