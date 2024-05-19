package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build

fun autoSetConnectionState(context: Context, viewModel: MyViewModel, url: String) {

    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        cm.registerDefaultNetworkCallback(mNetworkCallback(viewModel, url, context = context))
        viewModel.isConnected.value = cm.activeNetwork != null
    }

}

private fun mNetworkCallback(
    viewModel: MyViewModel,
    url: String,
    context: Context
): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.updateConnectifityStatus(true)
            if (viewModel.wordpressList.isEmpty()) {
                viewModel.updateHomeData(url, context.cacheDir)
            }
        }

        override fun onLost(network: Network) {
            viewModel.updateConnectifityStatus(false)
        }
    }
}
