package de.schweininchen.shintaikan.shintaikan.jetpack

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import java.io.File

fun getHttpJson(
    getURL: String, cacheDir: File, onError: (VolleyError) -> Unit = {
        Log.e(
            "NetworkSachen.kt",
            "getHttpJson: ${it.message}",
        )
    }, setText: (JSONArray) -> Unit
) {
    // Instantiate the cache
    val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

    // Set up the network to use HttpURLConnection as the HTTP client.
    val network = BasicNetwork(HurlStack())

    // Instantiate the RequestQueue with the cache and network. Start the queue.
    val requestQueue = RequestQueue(cache, network).apply {
        start()
    }

    val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, getURL, null,
        { response ->
            setText(response)
        },
        { error ->
            onError(error)
        }
    )

    requestQueue.add(jsonObjectRequest)

}
