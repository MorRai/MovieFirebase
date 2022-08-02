package com.rai.movieposter

import android.app.Application
import com.rai.movieposter.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviePosterApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoviePosterApplication)
            modules(
                viewModelsModule
            )
        }
    }
}