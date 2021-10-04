package com.kapil.android.youlearn.api.db

import androidx.room.TypeConverter
import com.kapil.android.youlearn.models.*

class Converters {
    @TypeConverter
    fun fromTabs(stringListString: String?): List<String>? {
        return stringListString?.split(",")?.map { it }
    }

    @TypeConverter
    fun toTags(stringList: List<String>?): String?{
        return stringList?.joinToString(separator = ",")
    }
}