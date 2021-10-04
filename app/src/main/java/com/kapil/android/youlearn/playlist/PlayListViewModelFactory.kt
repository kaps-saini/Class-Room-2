package com.kapil.android.youlearn.playlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.repository.Repository

class PlayListViewModelFactory(private var repository: Repository
)
    :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayListViewModel(repository) as T
    }
}