package com.example.ecomfire.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomfire.R
import com.example.ecomfire.data.User
import com.example.ecomfire.utils.*
import com.example.ecomfire.utils.Constants.USER_COLLECTION
import com.example.ecomfire.viewmodels.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _userSignUp = MutableStateFlow<AppState<User>>(AppState.Unspecified())

    val signUpUser: Flow<AppState<User>> = _userSignUp

    // Channel for sending the signup state
    private val _validation = Channel<SignUpFieldState> ()
    val validation = _validation.receiveAsFlow()

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object{
       // const val SHOPPING_ACTIVITY = 23
        const val LOGIN_FRAGMENT = R.id.action_signUpFragment_to_loginFragment
    }

    fun signUpUserOnFirebase(user: User, password: String) {
        // check for validation in an if block
        if (checkValidation(user, password)){
            runBlocking {
                _userSignUp.emit(AppState.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { it ->
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                       // _userSignUp.value = AppState.Success(user)
                        viewModelScope.launch {
                            _navigate.emit(LOGIN_FRAGMENT)
                        }
                    }

                }.addOnFailureListener {
                    _userSignUp.value = AppState.Error(it.message.toString())
                }
        }else{
            // return sign up field state  here
            val signUpFiledState = SignUpFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
            // both runBlocking and viewModeScope can be used to send the signup filed state
            viewModelScope.launch {
                _validation.send(signUpFiledState) // after here go and handle the validation check in the fragment
            }

        }


    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _userSignUp.value = AppState.Success(user)
            }.addOnFailureListener {
                _userSignUp.value = AppState.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String) : Boolean{
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val isValidSignUp = emailValidation is SignUpValidation.Success
                && passwordValidation is SignUpValidation.Success

        return isValidSignUp
    }
}