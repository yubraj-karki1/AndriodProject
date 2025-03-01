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
