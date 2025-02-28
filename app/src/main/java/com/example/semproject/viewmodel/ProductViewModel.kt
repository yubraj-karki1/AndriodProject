package com.example.semproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.semproject.model.ProductModel
import com.example.semproject.repository.ProductRepository

class ProductViewModel(val repo : ProductRepository) {
    fun addProduct(productModel: ProductModel,
                   callback:(Boolean,String) -> Unit){
        repo.addProduct(productModel,callback)

    }

    fun updateProduct(productId:String,data : MutableMap<String,Any>,
                      callback: (Boolean,String) -> Unit){
        repo.updateProduct(productId,data,callback)
    }

    fun deleteProduct(productId: String,
                      callback: (Boolean, String) -> Unit){
        repo.deleteProduct(productId,callback)
    }


    var _products = MutableLiveData<ProductModel?>()
    var products = MutableLiveData<ProductModel?>()
        get()= _products

    var _allproducts = MutableLiveData<List<ProductModel>>()
    var allproducts = MutableLiveData<List<ProductModel>>()
        get()= _allproducts



    fun getProductById(productId: String){
        repo.getProductById(productId){
                model,success,message->
            if (success){
                _products.value = model
            }
        }
    }

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading

    fun getAllProducts(){
        _loading.value=true
        repo.getAllProduct{ product, success, message->
            if (success){
                _allproducts.value = product
                _loading.value=false
            }
        }
    }
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit){
        repo.uploadImage(context, imageUri, callback)
    }
}

