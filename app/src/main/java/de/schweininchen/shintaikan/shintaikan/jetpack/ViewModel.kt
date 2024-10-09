package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.schweininchen.shintaikan.shintaikan.jetpack.util.cutTo
import de.schweininchen.shintaikan.shintaikan.jetpack.util.getFirestoreData
import de.schweininchen.shintaikan.shintaikan.jetpack.util.getFirestoreTrplan
import de.schweininchen.shintaikan.shintaikan.jetpack.util.getHttpJson
import de.schweininchen.shintaikan.shintaikan.jetpack.util.identityMatrix
import de.schweininchen.shintaikan.shintaikan.jetpack.util.matrixAdd
import de.schweininchen.shintaikan.shintaikan.jetpack.util.matrixMultiply
import de.schweininchen.shintaikan.shintaikan.jetpack.util.shearZ
import de.schweininchen.shintaikan.shintaikan.jetpack.util.xRotation
import de.schweininchen.shintaikan.shintaikan.jetpack.util.yRotation
import de.schweininchen.shintaikan.shintaikan.jetpack.util.zRotation
import kotlinx.coroutines.launch
import org.json.JSONException
import kotlin.math.PI
import kotlin.math.sqrt

class MyViewModel : ViewModel() {
    //-> Credits to Mr-Pine
    // Source: https://github.com/Mr-Pine/XKCDFeed/tree/9f4b95307822062ed74e251f2ac00d55d6d4d26b
    var matrix by mutableStateOf(identityMatrix(4, 4))

    //No clue why these values work but I found them to work best but only if I run the Matrix multiply on them twice
    private val wR = 50f
    private val wG = 10f
    private val wB = 4f

    init { //implementation of http://www.graficaobscura.com/matrix/index.html
        //RGB invert
        matrix = matrix.matrixMultiply(
            arrayOf(
                floatArrayOf(-1f, 0f, 0f, 0f),
                floatArrayOf(0f, -1f, 0f, 0f),
                floatArrayOf(0f, 0f, -1f, 0f),
                floatArrayOf(0f, 0f, 0f, -1f),
            )
        )
        matrix = matrix.matrixAdd(
            arrayOf(
                floatArrayOf(0f, 0f, 0f, 0f),
                floatArrayOf(0f, 0f, 0f, 0f),
                floatArrayOf(0f, 0f, 0f, 0f),
                floatArrayOf(255f, 255f, 255f, 0f),
            )
        )

        //HSV 180 rotation
        matrix = matrix.matrixMultiply(xRotation(cos = 1 / sqrt(2f), sin = 1 / sqrt(2f)))
        matrix = matrix.matrixMultiply(yRotation(cos = sqrt(2 / 3f), sin = -sqrt(1 / 3f)))
        val transformedWeights =
            arrayOf(floatArrayOf(wR, wG, wB)).matrixMultiply(matrix.cutTo(3, 3))
                .matrixMultiply(matrix.cutTo(3, 3))
        val shearX = (transformedWeights[0][0] / transformedWeights[0][2])
        val shearY = (transformedWeights[0][1] / transformedWeights[0][2])
        matrix = matrix.matrixMultiply(shearZ(shearX, shearY))
        matrix = matrix.matrixMultiply(zRotation(PI.toFloat()))
        matrix = matrix.matrixMultiply(shearZ(-shearX, -shearY))
        matrix = matrix.matrixMultiply(yRotation(cos = sqrt(2 / 3f), sin = sqrt(1 / 3f)))
        matrix = matrix.matrixMultiply(xRotation(cos = 1 / sqrt(2f), sin = -1 / sqrt(2f)))
    }
    //<- Credits to Mr-Pine

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

    fun updateHomeData(url: String, context: Context) {
        viewModelScope.launch {
            getHttpJson(url, context) {
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