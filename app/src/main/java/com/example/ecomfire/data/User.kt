package com.example.ecomfire.data

data class User(
    val firstName:String,
    val lastName:String,
    val email:String,
    val userImagePath:String ="",

){
    constructor(): this("","","","",)
}
