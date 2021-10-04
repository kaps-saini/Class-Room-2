package com.kapil.android.youlearn.models.search


import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("kind")
    val searchKind: String? = null,
    @SerializedName("videoId")
    val searchVideoId: String? = null
)