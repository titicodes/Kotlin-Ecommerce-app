package com.example.ecomfire.utils



import android.util.Patterns

fun validateEmail(email: String): SignUpValidation{
    if (email.isEmpty())
        return SignUpValidation.Failed("")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return SignUpValidation.Failed("Wrong email format")

    return SignUpValidation.Success
}

fun validatePassword(password: String): SignUpValidation{
    if (password.isEmpty())
        return SignUpValidation.Failed("Password cannot be empty")

    if (password.length < 6)
        return SignUpValidation.Failed("Password should contains 6 char")

    return SignUpValidation.Success
}