package com.kapil.android.youlearn.models.search


import com.google.gson.annotations.SerializedName

data class Default(
    @SerializedName("height")
    var defaultHeight: Int? = null,
    @SerializedName("url")
    var defaultUrl: String? = null,
    @SerializedName("width")
    var defaultWidth: Int? = null
)