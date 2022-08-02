package com.rai.movieposter.ui.addMovie

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.movieposter.data.Movie
import com.rai.movieposter.services.FirebaseMovieService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddMovieViewModel: ViewModel() {
    suspend fun  uploadImage(uri: Uri): Flow<String> {
        return  FirebaseMovieService.uploadImageWithUri(uri)
    }

    private fun addToMovie(movie: Movie) {
        viewModelScope.launch {
            FirebaseMovieService.addToCart(movie)
        }
    }

    private fun getUpdatedItemEntry(
        nameMovie: String,
        movieDate: Int,
        genres: List<String>,
        ratingImbd: Float,
        ratingKinopoisk: Float,
        certificate: String,
        countryOfOrigin: String,
        runtime: String,
        storyline: String,
        image: String,
        trailer: String,
    ): Movie {
        return Movie(
            nameMovie = nameMovie,
            movieDate = movieDate,
            genres = genres,
            ratingImbd = ratingImbd,
            ratingKinopoisk = ratingKinopoisk,
            certificate = certificate,
            countryOfOrigin = countryOfOrigin,
            runtime = runtime,
            storyline = storyline,
            image = image,
            trailer = trailer
        )
    }

    fun updateItem(
        nameMovie: String,
        movieDate: Int,
        genres: List<String>,
        ratingImbd: Float,
        ratingKinopoisk: Float,
        certificate: String,
        countryOfOrigin: String,
        runtime: String,
        storyline: String,
        image: String,
        trailer: String,
    ) {
        val updatedItem = getUpdatedItemEntry(nameMovie,
            movieDate,
            genres,
            ratingImbd,
            ratingKinopoisk,
            certificate,
            countryOfOrigin,
            runtime,
            storyline,
            image,
            trailer)
        addToMovie(updatedItem)
    }
}