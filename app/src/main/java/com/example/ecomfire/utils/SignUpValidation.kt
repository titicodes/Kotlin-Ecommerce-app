package com.example.ecomfire.utils

sealed class SignUpValidation  {
    object Success:SignUpValidation()
    data class Failed(val message:String):SignUpValidation()
}

data class SignUpFieldState(
    val email : SignUpValidation,
    val password:SignUpValidation
)