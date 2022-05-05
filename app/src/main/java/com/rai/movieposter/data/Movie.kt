package com.rai.movieposter.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Movie(
    val nameMovie: String? = null,
    val movieDate: Int? = null,
    val genres: List<String>? = null,
    val ratingImbd: Float? = null,
    val ratingKinopoisk: Float? = null,
    val certificate: String? = null, //возрастное ограничение
    val countryOfOrigin: String? = null, // страна выпуска
    val runtime: String? = null,
    val storyline: String? = null, // описание
    val image: String? = null,
    val trailer: String? = null
): Parcelable