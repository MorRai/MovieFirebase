package com.rai.movieposter.ui.detailMovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.movieposter.data.Response
import com.rai.movieposter.services.FirebaseMovieService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class MovieDetailViewModel(movieName: String) : ViewModel() {

    val movieFlow = flow {
        val movie = FirebaseMovieService.getMovie(movieName)
        emit(movie)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Response.Loading
    )
}