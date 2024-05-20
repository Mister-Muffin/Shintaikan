package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

fun getHttpJson(
    getURL: String, context: Context, onError: (VolleyError) -> Unit = {
        Log.e(
            "NetworkSachen.kt",
            "getHttpJson: ${it.message}",
        )
    }, setText: (JSONArray) -> Unit
) {
    val requestQueue = Volley.newRequestQueue(context)

    val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, getURL, null,
        { response ->
            setText(response)
        },
        { error ->
            onError(error)
        }
    )
    jsonObjectRequest.setShouldCache(false)
    requestQueue.add(jsonObjectRequest)

}
