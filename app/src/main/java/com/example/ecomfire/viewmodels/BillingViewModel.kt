package com.example.ecomfire.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomfire.data.Address
import com.example.ecomfire.utils.AppState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _address = MutableStateFlow<AppState<List<Address>>>(AppState.Unspecified())
    val address = _address.asStateFlow()

    init {
        getUserAddresses()
    }

    fun getUserAddresses() {
        viewModelScope.launch { _address.emit(AppState.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("address")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch { _address.emit(AppState.Error(error.message.toString())) }
                    return@addSnapshotListener
                }
                val addresses = value?.toObjects(Address::class.java)
                viewModelScope.launch { _address.emit(AppState.Success(addresses!!)) }
            }
    }

}