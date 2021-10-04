package com.kapil.android.youlearn.models.search


import com.google.gson.annotations.SerializedName

data class High(
    @SerializedName("height")
    var highHeight: Int? = null,
    @SerializedName("url")
    var highUrl: String? = null,
    @SerializedName("width")
    var highWidth: Int? = null
)