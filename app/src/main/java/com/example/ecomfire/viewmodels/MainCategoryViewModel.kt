package com.example.ecomfire.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomfire.data.Product
import com.example.ecomfire.utils.AppState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<AppState<List<Product>>>(AppState.Unspecified())
    val specialProducts: StateFlow<AppState<List<Product>>> = _specialProducts

    private val _bestDealsProducts =
        MutableStateFlow<AppState<List<Product>>>(AppState.Unspecified())
    val bestDealsProducts: StateFlow<AppState<List<Product>>> = _bestDealsProducts

    private val _bestProducts = MutableStateFlow<AppState<List<Product>>>(AppState.Unspecified())
    val bestProducts: StateFlow<AppState<List<Product>>> = _bestProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }

    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(AppState.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Special Products").get().addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(AppState.Success(specialProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(AppState.Error(it.message.toString()))
                }
            }
    }


    fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDealsProducts.emit(AppState.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDealsProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealsProducts.emit(AppState.Success(bestDealsProducts))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealsProducts.emit(AppState.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProducts.emit(AppState.Loading())
                firestore.collection("Products").limit(pagingInfo.bestProductsPage * 10).get()
                    .addOnSuccessListener { result ->
                        val bestProducts = result.toObjects(Product::class.java)
                        pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                        pagingInfo.oldBestProducts = bestProducts
                        viewModelScope.launch {
                            _bestProducts.emit(AppState.Success(bestProducts))
                        }
                        pagingInfo.bestProductsPage++
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            _bestProducts.emit(AppState.Error(it.message.toString()))
                        }
                    }
            }
        }
    }
}

internal data class PagingInfo(
    var bestProductsPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)




