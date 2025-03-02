package com.example.semproject.model

data class CartModel(
    var cartItemId: String = "",
    var productId: String = "",
    var quantity: Int = 1,
    var addedAt: Long = 0L,
    var product: ProductModel? = null
) {
    // Empty constructor for Firebase
    constructor() : this("", "", 1, 0L, null)
}
