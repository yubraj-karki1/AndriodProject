package com.example.semproject.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.semproject.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class ProductRepositoryImpl: ProductRepository {

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    //Table instant

    val ref : DatabaseReference = database.reference.

    child("products")
    override fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        var id = ref.push().key.toString()
        productModel.productId = id
        ref.child(id).setValue(productModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Added Successfully")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun updateProduct(
        productId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(productId).updateChildren(data).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Update Successfully")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        ref.child(productId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Deleted Successfully")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun getProductById(
        productId: String,
        callback: (ProductModel?, Boolean, String) -> Unit
    ) {
        ref.child(productId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var model = snapshot.getValue(ProductModel::class.java)
                    callback(model, true, "Product Fetched Successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message)
            }
        })
    }

    override fun getAllProducts(callback: (List<ProductModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var products = mutableListOf<ProductModel>()
                    for(eachData in snapshot.children){
                        var model = eachData.getValue(ProductModel::class.java)
                        if(model != null){
                            products.add(model)
                        }
                    }
                    callback(products,true, "products fetched success")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message)
            }

        })
    }

    override fun addToCart(productId: String, userId: String, callback: (Boolean) -> Unit) {
        val cartRef = database.reference.child("carts").child(userId)

        // Fetch product details first
        getProductById(productId) { product, success, message ->
            if (success && product != null) {
                // Check if product already exists in the cart
                getCartItem(cartRef, productId) { existingQuantity ->
                    if (existingQuantity != null) {
                        // If exists, increase quantity
                        cartRef.child(productId).child("quantity").setValue(existingQuantity + 1)
                            .addOnSuccessListener { callback(true) }
                            .addOnFailureListener { callback(false) }
                    } else {
                        // If new, add product to cart
                        val cartItem = mapOf(
                            "productId" to productId,
                            "productName" to product.productName,
                            "productPrice" to product.price,
                            "addedAt" to System.currentTimeMillis(),
                            "quantity" to 1
                        )
                        cartRef.child(productId).setValue(cartItem)
                            .addOnSuccessListener { callback(true) }
                            .addOnFailureListener { callback(false) }
                    }
                }
            } else {
                callback(false) // Product not found
            }
        }
    }


    private fun getCartItem(cartRef: DatabaseReference, productId: String, callback: (Int?) -> Unit) {
        cartRef.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val quantity = snapshot.child("quantity").getValue(Int::class.java) ?: 1
                    callback(quantity) // Return existing quantity
                } else {
                    callback(null) // Product not in cart
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null) // Handle errors by returning null
            }
        })
    }


    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "docqwxgzc",
            "api_key" to "636754294934794",
            "api_secret" to "XSmpfUXKDLUViiMbHMWf0bRSt30"
        )
    )

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

}
