fun getAllProducts(){
    _loading.value=true
    repo.getAllProduct{ product, success, message->
        if (success){
            _allproducts.value = product
            _allproducts.postValue(product) // Notify observers
            _loading.value=false
        }
    }
}
