package com.rai.movieposter.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.rai.movieposter.MovieViewModel


val viewModelsModule = module {
    viewModelOf(::MovieViewModel)
}