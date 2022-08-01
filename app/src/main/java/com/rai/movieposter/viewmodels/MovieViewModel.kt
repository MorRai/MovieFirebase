package com.rai.movieposter


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rai.movieposter.services.FirebaseMovieService
import com.rai.movieposter.data.Filters
import com.rai.movieposter.data.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class MovieViewModel(private val firebaseMovieService: FirebaseMovieService) : ViewModel() {

     private val mFilters: Filters? = null

     val filterQuery  = MutableStateFlow(mFilters)
     private val movieDataFlow = filterQuery.flatMapLatest {
          firebaseMovieService.getProfileData(it)
     }
     val movieData = movieDataFlow.asLiveData()

     suspend fun  uploadImage(uri: Uri): Flow<String>{
          return  firebaseMovieService.uploadImageWithUri(uri)
     }

     private fun addToMovie(movie: Movie) {
          viewModelScope.launch {
               firebaseMovieService.addToCart(movie)
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



