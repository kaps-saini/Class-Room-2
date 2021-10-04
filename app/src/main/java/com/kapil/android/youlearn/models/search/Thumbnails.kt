package com.kapil.android.youlearn.models.search

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Thumbnails(
    @SerializedName("default")
    @Embedded
    var default: Default,
    @SerializedName("high")
    @Embedded
    var high: High,
    @SerializedName("medium")
    @Embedded
    var medium: Medium
){
    constructor(): this(
        Default(0, "", 0),
        High(0, "", 0),
        Medium(0, "", 0),
    )
}
