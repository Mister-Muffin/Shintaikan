package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build

fun abc(context: Context, _viewModel: MyViewModel) {

    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        cm.registerDefaultNetworkCallback(mNetworkCallback(_viewModel, context = context))
        _viewModel.isConnected.value = cm.activeNetwork !== null
    }
    
}

private fun mNetworkCallback(
    viewModel: MyViewModel,
    context: Context
): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.updateConnectifityStatus(true)
            if (viewModel.wordpressList.isEmpty()) {
                viewModel.updateHomeData(
                    "https://shintaikan.de/?rest_route=/wp/v2/posts",
                    context.cacheDir
                )
            }
        }

        override fun onLost(network: Network) {
            viewModel.updateConnectifityStatus(false)
        }
    }
}