// ...existing code...
    override fun addToCart(productId: String, userId: String, callback: (Boolean, String) -> Unit) {
        // Add directly to the cart's product node
        val cartItemRef = database.reference
            .child("carts")
            .child(userId)
            .child(productId)

        val cartItem = mapOf(
            "productId" to productId,
            "addedAt" to System.currentTimeMillis(),
            "quantity" to 1
        )

        cartItemRef.setValue(cartItem)
            .addOnSuccessListener {
                callback(true, "Product added to cart successfully")
            }
            .addOnFailureListener { e ->
                callback(false, "Failed to add to cart: ${e.message}")
            }
    }

    override fun getCartItems(userId: String, callback: (List<CartModel>, Boolean, String) -> Unit) {
        val cartRef = database.reference.child("carts").child(userId)
        
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartModel>()
                var pendingItems = snapshot.childrenCount

                if (!snapshot.exists() || pendingItems == 0L) {
                    callback(cartItems, true, "Cart is empty")
                    return
                }

                for (cartSnapshot in snapshot.children) {
                    try {
                        val productId = cartSnapshot.child("productId").getValue(String::class.java) ?: cartSnapshot.key
                        if (productId != null) {
                            val cartItem = CartModel(
                                cartItemId = cartSnapshot.key ?: "",
                                productId = productId,
                                quantity = cartSnapshot.child("quantity").getValue(Long::class.java)?.toInt() ?: 1,
                                addedAt = cartSnapshot.child("addedAt").getValue(Long::class.java) ?: 0L
                            )

                            // Fetch the product details
                            getProductById(productId) { product, success, _ ->
                                if (success && product != null) {
                                    cartItem.product = product
                                    cartItems.add(cartItem)
                                }
                                pendingItems--
                                if (pendingItems == 0L) {
                                    cartItems.sortByDescending { it.addedAt }
                                    callback(cartItems, true, "Cart items fetched successfully")
                                }
                            }
                        } else {
                            pendingItems--
                        }
                    } catch (e: Exception) {
                        pendingItems--
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList(), false, error.message)
            }
        })
    }
// ...existing code...
