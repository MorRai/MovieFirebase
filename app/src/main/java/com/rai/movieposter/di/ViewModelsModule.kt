package com.rai.movieposter.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.rai.movieposter.ui.addMovie.AddMovieViewModel
import com.rai.movieposter.ui.listMovies.ListMovieViewModel

val viewModelsModule = module {
    viewModelOf(::AddMovieViewModel)
    viewModelOf(::ListMovieViewModel)
}