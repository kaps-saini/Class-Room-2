package com.kapil.android.youlearn.models.search


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class SearchList(
    @SerializedName("etag")
    val etag: String? = null,
    @SerializedName("items")
    @Embedded
    val items: List<Item>,
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    @SerializedName("pageInfo")
    @Embedded
    val pageInfo: PageInfo,
    @SerializedName("regionCode")
    val regionCode: String? = null
)