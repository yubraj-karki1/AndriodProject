package com.example.semproject.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semproject.R
import com.example.semproject.adapter.ProductAdapter
import com.example.semproject.databinding.FragmentHomeBinding
import com.example.semproject.repository.ProductRepository
import com.example.semproject.repository.ProductRepositoryImpl
import com.example.semproject.ui.activity.AddProductActivity
import com.example.semproject.viewmodel.ProductViewModel



    class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        productViewModel.getAllProducts() // Fetch latest products
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel and Adapter
        val repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)
        adapter = ProductAdapter(requireContext(), ArrayList())

        // Set up RecyclerView
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

        // Observe products from ViewModel
        productViewModel.allproducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                adapter.updateData(it)
            }
        }

        // Observe loading state
        productViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Fetch all products
        productViewModel.getAllProducts()

        // Set up FAB to navigate to AddProductActivity
        binding.floatingBtn.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }}}


