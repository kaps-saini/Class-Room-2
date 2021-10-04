package com.kapil.android.youlearn.repository

import com.kapil.android.youlearn.api.RetrofitInstance
import com.kapil.android.youlearn.api.db.ItemDataBase
import com.kapil.android.youlearn.models.search.Item
import com.kapil.android.youlearn.models.search.SearchList
import retrofit2.Response

class Repository(private var db: ItemDataBase) {

    suspend fun getDataV3( options: Map<String, String>): Response<SearchList> {
        return RetrofitInstance.api.getV3(options)
    }

    suspend fun upsert(item: Item) = db.getItemDataBase().insert(item)

    fun getAddedVideos() = db.getItemDataBase().getAllData()

    fun getAllAddedVideosId(id : String?) = db.getItemDataBase().getAllVideoId(id)

    suspend fun updateVideos(item: Item) = db.getItemDataBase().update(item)

    suspend fun deleteVideos(item: Item) = db.getItemDataBase().delete(item)
}