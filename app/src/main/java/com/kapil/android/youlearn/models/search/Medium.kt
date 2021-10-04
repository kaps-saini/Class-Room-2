package com.kapil.android.youlearn.models.search


import com.google.gson.annotations.SerializedName

data class Medium(
    @SerializedName("height")
    var mediumHeight: Int? = null,
    @SerializedName("url")
    var mediumUrl: String? = null,
    @SerializedName("width")
    var mediumWidth: Int? = null
)