package com.kapil.android.youlearn.youtubeplayer

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.repository.Repository

class YTPViewModelFactory(private val repository: Repository, ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return YouTubePlayerViewModel(repository) as T
    }
}