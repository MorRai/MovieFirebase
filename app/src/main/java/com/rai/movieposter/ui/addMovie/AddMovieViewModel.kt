package com.rai.movieposter.ui.addMovie

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.movieposter.data.Movie
import com.rai.movieposter.data.Response
import com.rai.movieposter.services.FirebaseMovieService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URL

class AddMovieViewModel(movieName: String) : ViewModel() {

    suspend fun uploadImage(uri: Uri, oldUrl:String?): Flow<Response<String>> {
        return FirebaseMovieService.uploadImageWithUri(uri,oldUrl)
    }

    val movieFlow = flow {
        if (movieName == "addMovie") {
            emit(Response.Error("Create movie!"))
        } else {
            val movie = FirebaseMovieService.getMovie(movieName)
            emit(movie)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Response.Loading
    )

    private fun addToMovie(movie: Movie) {
        viewModelScope.launch {
            FirebaseMovieService.addToCart(movie)
        }
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
        val updatedItem = Movie(
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
        addToMovie(updatedItem)
    }


}