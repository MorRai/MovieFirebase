package com.rai.movieposter.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.rai.movieposter.MovieViewModel
import com.rai.movieposter.ui.listMovies.ListMovieViewModel

val viewModelsModule = module {
    viewModelOf(::MovieViewModel)
    viewModelOf(::ListMovieViewModel)
}