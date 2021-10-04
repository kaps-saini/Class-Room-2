package com.kapil.android.youlearn.models.search

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "SearchTable")
data class Item(

    @PrimaryKey(autoGenerate = true)
    var searchKey: Int? = null,

    @SerializedName("etag")
    val etag: String? = null,
    @SerializedName("id")
    @Embedded
    val id: Id,
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("snippet")
    @Embedded
    val snippet: Snippet
) : Serializable