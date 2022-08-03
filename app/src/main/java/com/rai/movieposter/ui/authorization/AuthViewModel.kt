package com.rai.movieposter.ui.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.rai.movieposter.data.LceState
import com.rai.movieposter.repository.authenticator.FirebaseAuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository : FirebaseAuthRepository): ViewModel() {
    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = _firebaseUser

    private val eventsChannel = Channel<LceState>()
    val allEventsFlow = eventsChannel.receiveAsFlow()

    fun signInUser(email: String , password: String) = viewModelScope.launch{
        when {
            email.isEmpty() -> {
                eventsChannel.send(LceState.Error("email should not be empty"))
            }
            password.isEmpty() -> {
                eventsChannel.send(LceState.Error("password should not be empty"))
            }
            else -> {
                actualSignInUser(email , password)
            }
        }
    }

    fun signUpUser(email : String , password: String )= viewModelScope.launch {
        when{
            email.isEmpty() -> {
                eventsChannel.send(LceState.Error( "email should not be empty"))
            }
            password.isEmpty() -> {
                eventsChannel.send(LceState.Error("password should not be empty"))
            }
            email.length < 6 ->{
                eventsChannel.send(LceState.Error("password should be longer then 6 symbols"))
            }
            else -> {
                actualSignUpUser(email, password)
            }
        }
    }

    private fun actualSignInUser(email:String, password: String) = viewModelScope.launch {
        try {
            val user = repository.signInWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(LceState.Message("login success"))
            }
        }catch(e:Exception){
            val error = e.toString().split(":").toTypedArray()
            eventsChannel.send(LceState.Error(error[1]))
        }
    }

    private fun actualSignUpUser(email:String , password: String) = viewModelScope.launch {
        try {
            val user = repository.signUpWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(LceState.Message("sign up success"))
            }
        }catch(e:Exception){
            val error = e.toString().split(":").toTypedArray()
            eventsChannel.send(LceState.Error(error[1]))
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        val user = repository.getUser()
        _firebaseUser.postValue(user)
    }

}