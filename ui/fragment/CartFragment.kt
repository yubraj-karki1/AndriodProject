package com.example.semproject.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.semproject.adapter.CartAdapter
import com.example.semproject.databinding.FragmentCartBinding
import com.example.semproject.repository.ProductRepositoryImpl
import com.example.semproject.viewmodel.CartViewModel
import android.util.Log

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        cartViewModel = CartViewModel(ProductRepositoryImpl())
        
        // Initialize adapter
        cartAdapter = CartAdapter()
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        // Observe cart items
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            Log.d("CartFragment", "Received ${items.size} items")
            items.forEach { item ->
                Log.d("CartFragment", "Cart item: ${item.productId}, Product: ${item.product?.productName}")
            }
            cartAdapter.updateCartItems(items)
            binding.emptyCartMessage.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }

        // Observe loading state
        cartViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("CartFragment", "Loading state: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Load cart items with user ID "1" as shown in your Firebase structure
        cartViewModel.loadCartItems("1")
    }
}
