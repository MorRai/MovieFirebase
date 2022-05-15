package com.rai.movieposter


import android.net.Uri
import androidx.lifecycle.*
import com.rai.movieposter.services.FirebaseMovieService
import com.rai.movieposter.data.Filters
import com.rai.movieposter.data.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class MovieViewModel : ViewModel() {

     private val mFilters: Filters? = null

     val filterQuery  = MutableStateFlow(mFilters)
     private val movieDataFlow = filterQuery.flatMapLatest {
          FirebaseMovieService.getProfileData(it)
     }
     val movieData = movieDataFlow.asLiveData()

     suspend fun  uploadImage(uri: Uri): Flow<String>{
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

class MovieViewModelFactory : ViewModelProvider.Factory {
     override fun <T : ViewModel?> create(modelClass: Class<T>): T {
          if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
               @Suppress("UNCHECKED_CAST")
               return MovieViewModel() as T
          }
          throw IllegalArgumentException("Unknown ViewModel class")
     }
}


