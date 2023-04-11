package com.example.ecomfire.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomfire.data.User
import com.example.ecomfire.utils.AppState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel() {

    private val _login = MutableSharedFlow<AppState<FirebaseUser>> ()
    val  login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<AppState<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun loginWithEmailAndPassword(email:String, password:String){
        viewModelScope.launch { _login.emit(AppState.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    it.user?.let {
                        _login.emit(AppState.Success(it))
                    }
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _login.emit(AppState.Error(it.message.toString()))
                }
            }
    }
    fun resetPassword(email:String){
        viewModelScope.launch {
            _resetPassword.emit(AppState.Loading())
        }
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(AppState.Success(email))
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _resetPassword.emit(
                        AppState.Error(it.message.toString())
                    )
                }
            }
    }
}