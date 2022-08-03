package com.rai.movieposter.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.rai.movieposter.repository.authenticator.FirebaseAuthRepository

 val firebaseAuthRepositoryModule = module {
    singleOf(::FirebaseAuthRepository)
}