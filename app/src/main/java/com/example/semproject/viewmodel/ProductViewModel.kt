package com.example.semproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semproject.model.ProductModel
import com.example.semproject.repository.ProductRepository
import com.example.semproject.ui.fragment.CartFragment

class ProductViewModel(val repo: ProductRepository) {
    fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(productModel, callback)
    }

    fun updateProduct(productId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateProduct(productId, data, callback)
    }

    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(productId, callback)
    }

    var _products = MutableLiveData<ProductModel?>()
    var products = MutableLiveData<ProductModel?>()
        get() = _products

    var _allproducts = MutableLiveData<List<ProductModel>?>()
    var allproducts: MutableLiveData<out List<ProductModel>?> = MutableLiveData<List<ProductModel>>()
        get() = _allproducts

    fun getProductById(productId: String) {
        repo.getProductById(productId) { model, success, message ->
            if (success) {
                _products.value = model
            }
        }
    }

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading

    fun getAllProducts() {
        _loading.value = true
        repo.getAllProducts { product, success, message ->
            if (success) {
                _allproducts.value = product
                _loading.value = false
            }
        }
    }

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }

    // Add to Cart Implementation
    fun addToCart(productId: String, quantity: Int, callback: (Boolean, String) -> Unit) {
        repo.addToCart(productId, quantity.toString(), callback)
    }
}
