package com.example.semproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semproject.model.CartModel
import com.example.semproject.repository.ProductRepository

class CartViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartModel>>()
    val cartItems: LiveData<List<CartModel>> = _cartItems

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        // Start listening to cart updates when ViewModel is created
        loadCartItems("1")
    }

    fun loadCartItems(userId: String) {
        _loading.value = true
        repository.getCartItems(userId) { items, success, message ->
            _loading.value = false
            if (success) {
                _cartItems.value = items
            }
        }
    }
}
