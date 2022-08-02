package com.rai.movieposter.data

import kotlinx.serialization.Serializable

@Serializable
class Filters (
    var yearStart: Int?,
    var yearEnd: Int?,
    var imbdStart: Float?,
    var imbdEnd: Float?,
    var kinopoiskStart: Float?,
    var kinopoiskEnd: Float?,
    ):java.io.Serializable