package com.kapil.android.youlearn.api

import com.kapil.android.youlearn.models.search.SearchList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YouTubeApi {

    @GET("youtube/v3/search")
    suspend fun getV3(
        @QueryMap options: Map<String, String>
    ): Response<SearchList>
}