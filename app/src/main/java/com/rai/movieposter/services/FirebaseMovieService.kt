package com.rai.movieposter.services

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rai.movieposter.data.Filters
import com.rai.movieposter.data.Movie
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*


object FirebaseMovieService {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val firebaseStorageRef = FirebaseStorage.getInstance().reference
    private const val TAG = "FirebaseMovieService"
    private const val  COLLECTION_MOVIE = "movieData"

    fun getMoviesData(mFilters:Filters?): Flow<List<Movie>> {
        return callbackFlow {
            val listenerRegistration = db.collection(COLLECTION_MOVIE)
                .whereGreaterThan("movieDate",
                    mFilters?.yearStart ?: 0)
                .whereLessThan("movieDate",
                    mFilters?.yearEnd ?: 9999)
                .addSnapshotListener { documentSnapshots, exception ->
                    if (exception != null) {
                        Log.e("onEvent:error", exception.toString())
                        return@addSnapshotListener
                    }
                    val map = documentSnapshots!!.documents
                        .mapNotNull { it.toObject(Movie::class.java) }.filter {
                            (it.ratingImbd!! >= mFilters?.imbdStart ?: 0.0f) && (it.ratingImbd <= mFilters?.imbdEnd ?: 9999.0f)
                                    && (it.ratingKinopoisk!! >= mFilters?.kinopoiskStart ?: 0.0f) && (it.ratingKinopoisk <= mFilters?.kinopoiskEnd ?: 9999.0f)
                        }
                    trySend(map).isSuccess
                }
            awaitClose {
                Log.e(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }


     fun addToCart(movie: Movie){
        db.collection(COLLECTION_MOVIE)
            .document(movie.nameMovie!!)
            .set(movie)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }


    suspend  fun uploadImageWithUri(
        uri: Uri,
    ) : Flow<String> {

        return callbackFlow {
            val ref =
                firebaseStorageRef.child("images/" + UUID.randomUUID().toString())

            val uploadTask = ref.putFile(uri)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        Log.e(TAG, "Error image listener")
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "Error image listener")
                    return@addOnCompleteListener
                } else {
                    val downloadUri = task.result.toString()
                    trySend(downloadUri).isSuccess
                }
            }
            awaitClose {
                Log.e(TAG, "Cancelling posts listener")
            }



        }
    }



}
