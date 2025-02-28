package com.example.semproject.model

interface ProductRepository {

    fun addProduct(productModel: ProductModel,
                   callback:(Boolean,String) -> Unit)

    fun updateProduct(productId:String,data : MutableMap<String,Any>,
                      callback: (Boolean,String) -> Unit)

    fun deleteProduct(productId: String,
                      callback: (Boolean, String) -> Unit)

    fun getProductById(productId: String,
                       callback: (ProductModel?,Boolean, String)
                       -> Unit)

    fun getAllProducts(callback: (List<ProductModel>?,
                                  Boolean, String) -> Unit)
}