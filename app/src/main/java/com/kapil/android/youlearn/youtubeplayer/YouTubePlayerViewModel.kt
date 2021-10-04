package com.kapil.android.youlearn.youtubeplayer

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.kapil.android.youlearn.models.search.Item
import com.kapil.android.youlearn.models.search.SearchList
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.network.Resource
import com.kapil.android.youlearn.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import retrofit2.Response
import java.io.IOException

class YouTubePlayerViewModel(
    private val repository: Repository) : ViewModel(){

     val responseV3 : MutableLiveData<Resource<SearchList>> = MutableLiveData()
     private var allIds : MutableLiveData<List<Item>> = MutableLiveData()

    fun getV3(options: Map<String, String>){
        viewModelScope.launch {
            responseV3.postValue(Resource.Loading())
            safeGetNews(options)
        }
    }

    fun addVideos(item: Item) = viewModelScope.launch {
        repository.upsert(item)
    }

    fun getAddedVideos() = repository.getAddedVideos()

    fun updateVideo(item: Item) = viewModelScope.launch {
        repository.updateVideos(item)
    }

    fun deleteVideos(item: Item) = viewModelScope.launch {
        repository.deleteVideos(item)
    }

    private suspend fun safeGetNews(options: Map<String, String>){
        responseV3.postValue(Resource.Loading())
        try {
                val response = repository.getDataV3(options)
                responseV3.postValue(handleApiResponse(response))
        }catch (t: Throwable){
          when(t){
              is IOException -> {
                  responseV3.postValue(Resource.Error("Network Error"))
              }else -> {
                  responseV3.postValue(Resource.Error("Conversion Error"))
              }
          }
        }
    }

    private fun handleApiResponse(response: Response<SearchList>) : Resource<SearchList>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }else{
            return Resource.Error(response.message().toString())
        }
        return Resource.Error(response.message())
    }

}
