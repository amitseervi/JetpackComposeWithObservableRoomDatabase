package com.amit.radiuscompose.model.network

import android.content.Context
import android.net.ConnectivityManager


object NetworkUtil {
    fun isInternetAvailable(context: Context): Boolean {
        val netInfo =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}