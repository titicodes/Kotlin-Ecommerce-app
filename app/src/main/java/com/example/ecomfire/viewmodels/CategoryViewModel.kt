package com.example.ecomfire.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomfire.data.Category
import com.example.ecomfire.data.Product
import com.example.ecomfire.utils.AppState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    private val _offerProducts = MutableStateFlow<AppState<List<Product>>>(AppState.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<AppState<List<Product>>>(AppState.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    fun fetchOfferProducts() {
        viewModelScope.launch {
            _offerProducts.emit(AppState.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(AppState.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(AppState.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(AppState.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", category.category)
            .whereEqualTo("offerPercentage", null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(AppState.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(AppState.Error(it.message.toString()))
                }
            }
    }

}