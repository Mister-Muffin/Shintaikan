package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch
import java.io.File

class MyViewModel : ViewModel() {

    //<editor-fold desc="Pull to refresh">
    val swipeResfreshState = SwipeRefreshState(isRefreshing = false)

    fun setRefresh(refreshing: Boolean) {
        swipeResfreshState.isRefreshing = refreshing
    }
    //</editor-fold>

    //<editor-fold desc="Trplan">
    var trplanData by mutableStateOf(mapOf<String, MutableMap<String, Any>>())

    fun updateTrplan() {
        getFirestoreTrplan {
            trplanData = it
        }
    }
    //</editor-fold>

    //<editor-fold desc="HomeData">
    val wordpressList = mutableStateListOf<Array<String>>()

    fun updateHomeData(url: String, cacheDir: File) {
        viewModelScope.launch {
            getHttpJson(url, cacheDir) {
                wordpressList.clear()
                for (i in 0 until it.length()) {
                    wordpressList.add(
                        arrayOf(
                            it.getJSONObject(i).getJSONObject("title").getString("rendered"),
                            it.getJSONObject(i).getJSONObject("content").getString("rendered")
                        )
                    )
                }
            }
        }
    }

    var isConnected by mutableStateOf(true)

    //</editor-fold>

    //<editor-fold desc="Firestore Data">
    val firestoreData = mutableStateMapOf<String, MutableMap<String, Any>>()

    fun updateFirestoreData(function: () -> Unit = {}) {
        getFirestoreData {
            firestoreData.clear()
            firestoreData.putAll(it)
            function()
        }
    }
    //</editor-fold>

    var firebaseMessagingToken by mutableStateOf("NO_TOKEN")
        private set

    fun updatefirebaseMessagingToken(token: String) {
        firebaseMessagingToken = token
    }

}