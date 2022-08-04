package com.rai.movieposter.ui.listMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rai.movieposter.data.Filters
import com.rai.movieposter.data.LceState
import com.rai.movieposter.repository.authenticator.FirebaseAuthRepository
import com.rai.movieposter.services.FirebaseMovieService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListMovieViewModel(private val repository : FirebaseAuthRepository) : ViewModel()  {
    private val eventsChannel = Channel<LceState>()
    val allEventsFlow = eventsChannel.receiveAsFlow()

    private val filter = Filters(null, null, null, null, null, null)
    private val filterQuery = MutableStateFlow(filter)

    private val movieDataFlow = filterQuery.flatMapLatest {
        FirebaseMovieService.getMoviesData(it)
    }
    val movieData = movieDataFlow.asLiveData()

    fun onFilterChanged(filter: Filters) {
        filterQuery.value = filter
    }

    fun getFilter():Filters {
        return filterQuery.value
    }

    fun signOut() = viewModelScope.launch {
        try {
            val user = repository.signOut()
            user?.let {
                eventsChannel.send(LceState.Message("logout failure"))
            }?: eventsChannel.send(LceState.Message("sign out successful"))

        }catch(e:Exception){
            val error = e.toString().split(":").toTypedArray()
            eventsChannel.send(LceState.Error(error[1]))
        }
    }

}