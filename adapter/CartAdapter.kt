package com.example.semproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semproject.R
import com.example.semproject.model.CartModel

class CartAdapter(private var cartItems: List<CartModel> = listOf()) : 
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.cartItemImage)
        val productName: TextView = view.findViewById(R.id.cartItemName)
        val productPrice: TextView = view.findViewById(R.id.cartItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        Log.d("CartAdapter", "Binding item at position $position: ${cartItem.productId}")
        
        cartItem.product?.let { product ->
            holder.productName.text = product.productName
            holder.productPrice.text = "$${product.price}"
            
            Log.d("CartAdapter", "Loading image URL: ${product.imageUrl}")
            // Load image using Glide
            Glide.with(holder.productImage.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.productImage)
        } ?: run {
            Log.e("CartAdapter", "Product is null for cart item: ${cartItem.productId}")
            holder.productName.text = "Loading..."
            holder.productPrice.text = ""
        }
    }

    override fun getItemCount() = cartItems.size

    fun updateCartItems(newItems: List<CartModel>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}
