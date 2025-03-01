fun getAllProducts() {
    _loading.value = true
    repo.getAllProduct { products, success, message ->
        _loading.value = false  // Always update loading state
        when {
            success && products != null -> {
                _allproducts.value = products
            }
            else -> {
                _error.value = message ?: "Unknown error occurred"
            }
        }
    }
}

private val _cartMessage = MutableLiveData<String>()
val cartMessage: LiveData<String> = _cartMessage

fun addToCart(productId: String, userId: String) {
    _loading.value = true
    repo.addToCart(productId, userId) { success, message ->
        _loading.value = false
        _cartMessage.value = message
    }
}
