package com.kapil.android.youlearn.playlist

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapil.android.youlearn.models.search.SearchList
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.network.Resource
import com.kapil.android.youlearn.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PlayListViewModel(private var repository: Repository):
    ViewModel(){

    val listResponse: MutableLiveData<Resource<SearchList>> = MutableLiveData()

    fun getPlayList(options: Map<String, String>){
        viewModelScope.launch {
            listResponse.postValue(Resource.Loading())
            getSafeResponse(options)
        }
    }

    private suspend fun getSafeResponse(options: Map<String, String>){
        listResponse.postValue(Resource.Loading())
        try {
                val response = repository.getDataV3(options)
                listResponse.postValue(handleApiResponse(response))
        }catch (t: Throwable){
            Log.e("logMessage", t.message.toString())
            when(t){
                is IOException ->{
                    listResponse.postValue(Resource.Error("Network failure"))
                }
                else ->{
                    listResponse.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    private fun handleApiResponse(response: Response<SearchList>) : Resource<SearchList> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }else{
            return Resource.Error(response.errorBody().toString())
        }
        return Resource.Error(response.message())
    }

}