package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.File

class MyViewModel : ViewModel() {
    //<editor-fold desc="Trplan">
    val trplanData = mutableStateOf(mapOf<String, MutableMap<String, Any>>())

    fun updateTrplan() {
        getFirestoreTrplan {
            trplanData.value = it
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
                            try {
                                it.getJSONObject(i).getJSONObject("title").getString("rendered")
                            } catch (jsex: JSONException) {
                                "Ein Fehler ist aufgetreten!"
                            },
                            try {
                                it.getJSONObject(i).getJSONObject("content").getString("rendered")
                            } catch (jsex: JSONException) {
                                "Ein Fehler ist aufgetreten!"
                            },
                        )
                    )
                }
            }
        }
    }

    val isConnected = mutableStateOf(true)

    fun updateConnectifityStatus(isConnected: Boolean) {
        this.isConnected.value = isConnected
    }

    //</editor-fold>

    //<editor-fold desc="Firestore Data">
    val firestoreData = mutableStateMapOf<String, MutableMap<String, Any>>()

    fun updateFirestoreData(onFinished: () -> Unit = {}) {
        getFirestoreData {
            firestoreData.clear()
            firestoreData.putAll(it)
            onFinished()
        }
    }
    //</editor-fold>

    val firebaseMessagingToken = mutableStateOf("NO_TOKEN")

    fun updatefirebaseMessagingToken(token: String) {
        firebaseMessagingToken.value = token
    }

    //<editor-fold desc="List states">
    var lazyState = LazyListState()

    var lazyStateStart = LazyListState()
    var lazyStateTrplan = LazyListState()
    var lazyStatePruef = LazyListState()
    var lazyStateFerien = LazyListState()
    var lazyStateSoFe = LazyListState()
    var lazyStateClub = LazyListState()
    var lazyStateAnf = LazyListState()
    var lazyStatePres = LazyListState()
    var lazyStateTurn = LazyListState()
    var lazyStateColors = LazyListState()
    //</editor-fold>

}