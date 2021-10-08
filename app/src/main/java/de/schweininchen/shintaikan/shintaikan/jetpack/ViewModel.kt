package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class MyViewModel : ViewModel() {

    //<editor-fold desc="Pull to refresh">
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    suspend fun setRefresh(refreshing: Boolean) {
        _isRefreshing.emit(refreshing)
    }
    //</editor-fold>

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
    //</editor-fold>

    //<editor-fold desc="Firestore Data">
    val firestoreData = mutableStateOf(mapOf<String, MutableMap<String, Any>>())

    fun updateFirestoreData() {
        getFirestoreData {
            firestoreData.value = it
        }
    }
    //</editor-fold>

}