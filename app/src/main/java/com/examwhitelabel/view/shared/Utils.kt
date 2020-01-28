package com.examwhitelabel.view.shared

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

@Suppress("unused") // Required receiver parameter for the designed usage
fun Any?.exhaustive() = Unit

fun consume(action: () -> Unit) = true.also { action() }

inline fun Context.registerNetworkCallback(crossinline onConnected: () -> Unit, crossinline onDisconnected: () -> Unit) {
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).registerNetworkCallback(
        NetworkRequest.Builder().build(),
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onConnected()
            }

            override fun onLost(network: Network) {
                onDisconnected()
            }
        })
}

fun Context.isNetworkAvailable() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork != null
