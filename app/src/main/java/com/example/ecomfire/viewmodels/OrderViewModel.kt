package com.example.ecomfire.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomfire.data.order.Order
import com.example.ecomfire.utils.AppState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _order = MutableStateFlow<AppState<Order>>(AppState.Unspecified())
    val order = _order.asStateFlow()

    fun placeOrder(order: Order) {
        viewModelScope.launch {
            _order.emit(AppState.Loading())
        }
        firestore.runBatch { batch ->
            //TODO: Add the order into user-orders collection
            //TODO: Add the order into orders collection
            //TODO: Delete the products from user-cart collection

            firestore.collection("user")
                .document(auth.uid!!)
                .collection("orders")
                .document()
                .set(order)

            firestore.collection("orders").document().set(order)


            firestore.collection("user").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(AppState.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(AppState.Error(it.message.toString()))
            }
        }
    }

}



