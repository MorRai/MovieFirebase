package com.rai.movieposter.di

import com.rai.movieposter.services.FirebaseMovieService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieFirebaseRepositoryModule = module {
   // singleOf(::FirebaseMovieService)
}