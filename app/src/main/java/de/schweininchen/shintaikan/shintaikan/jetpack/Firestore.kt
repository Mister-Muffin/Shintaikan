package de.schweininchen.shintaikan.shintaikan.jetpack

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "Firestore.kt"

fun getFirestoreData(
    onError: (Exception) -> Unit = { Log.w(TAG, "Error getting documents.", it) },
    onSuccess: (MutableMap<String, MutableMap<String, Any>>) -> Unit
) {
    val db = Firebase.firestore

    db.collection("app")
        .get()
        .addOnSuccessListener { result ->
            val firestoreData: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
            for (document in result) {
                firestoreData[document.id] = document.data

                //Log.d(TAG, "${document.id} => ${document.data}")
            }
            onSuccess(firestoreData)
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }

}

fun getFirestoreTrplan(
    onError: (Exception) -> Unit = { Log.w(TAG, "Error getting documents.", it) },
    onSuccess: (MutableMap<String, MutableMap<String, Any>>) -> Unit
) {
    val db = Firebase.firestore

    db.collection("times")
        .get()
        .addOnSuccessListener { result ->
            val firestoreData: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
            for (document in result) {
                firestoreData[document.id] = document.data
                firestoreData[document.id]?.set("key", document.id)

                Log.d(TAG, "${document.id} => ${document.data}")
            }
            onSuccess(firestoreData)
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }

}