package com.rai.movieposter.data

sealed class LceState {
    data class Message(val message : String) : LceState()
    data class Error(val error : String) : LceState()
}