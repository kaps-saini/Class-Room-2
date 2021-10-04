package com.kapil.android.youlearn.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
     fun activeNetwork(context: Context?): Boolean {
         val checkConnectivity =
             context?.getSystemService(Context.CONNECTIVITY_SERVICE)!! as ConnectivityManager
         val activeNetworks: NetworkInfo? = checkConnectivity.activeNetworkInfo
         if (activeNetworks?.isConnected != null) {
             return activeNetworks.isConnected
         } else {
             return false
         }
         /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             val activeNetwork = checkConnectivity.activeNetwork ?: return false
             val capabilities =
                 checkConnectivity.getNetworkCapabilities(activeNetwork) ?: return false
             return when {
                 capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                 capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                 capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                 else -> false
             }
         } else {
             checkConnectivity.activeNetworkInfo?.run {
                 return when (type) {
                     ConnectivityManager.TYPE_WIFI -> true
                     ConnectivityManager.TYPE_MOBILE -> true
                     ConnectivityManager.TYPE_ETHERNET -> true
                     else -> false
                 }
             }
         }*/
    }
}