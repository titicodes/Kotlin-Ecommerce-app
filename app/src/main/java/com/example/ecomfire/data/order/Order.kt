package com.example.ecomfire.data.order

import android.os.Parcelable
import com.example.ecomfire.data.Address
import com.example.ecomfire.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId: Long = Random.nextLong(0, 100_000_000_000) + totalPrice.toLong()
): Parcelable