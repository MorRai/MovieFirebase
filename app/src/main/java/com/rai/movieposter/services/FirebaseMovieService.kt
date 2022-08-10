package com.rai.movieposter.services

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rai.movieposter.data.Filters
import com.rai.movieposter.data.Movie
import com.rai.movieposter.data.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*


object FirebaseMovieService {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val firebaseStorageRef = FirebaseStorage.getInstance().reference
    private const val TAG = "FirebaseMovieService"
    private const val COLLECTION_MOVIE = "movieData"

    fun getMoviesData(mFilters: Filters?): Flow<Response<List<Movie>>> {
        return callbackFlow {
            trySend(Response.Loading)

            val listenerRegistration = db.collection(COLLECTION_MOVIE)
                .whereGreaterThan("movieDate",
                    mFilters?.yearStart ?: 0)
                .whereLessThan("movieDate",
                    mFilters?.yearEnd ?: 9999)
                .limit(25)
                .addSnapshotListener { documentSnapshots, exception ->
                    val response = if (documentSnapshots != null) {
                        val movies = documentSnapshots.documents
                            .mapNotNull { it.toObject(Movie::class.java) }.filter {
                                (it.ratingImbd!! >= mFilters?.imbdStart ?: 0.0f) && (it.ratingImbd <= mFilters?.imbdEnd ?: 9999.0f)
                                        && (it.ratingKinopoisk!! >= mFilters?.kinopoiskStart ?: 0.0f) && (it.ratingKinopoisk <= mFilters?.kinopoiskEnd ?: 9999.0f)
                            }
                        Response.Success(movies)
                    } else {
                        Response.Error(exception?.message ?: exception.toString())
                    }
                    trySend(response).isSuccess
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getMovie(movieName: String): Response<Movie> {
        return try {
            val movie = db.collection(COLLECTION_MOVIE)
                .document(movieName).get().await().toObject(Movie::class.java)
            if (movie == null) Response.Error("Don't found movie!") else Response.Success(movie)
        } catch (exception: Exception) {
            Response.Error(exception.message ?: exception.toString())
        }
    }


    fun addToCart(movie: Movie) {
        db.collection(COLLECTION_MOVIE)
            .document(movie.nameMovie!!)
            .set(movie)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        //можно бы oтвет овзращать
    }


    suspend fun uploadImageWithUri(
        uri: Uri,
    ): Flow<Response<String>> = flow {
        try {
          emit(Response.Loading)
            val downloadUrl =  firebaseStorageRef.child("images/" + UUID.randomUUID().toString())
                .putFile(uri)
                .await()
                .storage.downloadUrl.await()
            emit(Response.Success(downloadUrl.toString()))
        }catch (e: Exception) {
            emit(Response.Error(e.message?:""))
        }
    }

}
