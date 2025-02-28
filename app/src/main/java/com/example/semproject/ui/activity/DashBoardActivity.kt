package com.example.semproject.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semproject.R
import com.example.semproject.adapter.ProductAdapter
import com.example.semproject.databinding.ActivityDashBoardBinding
import com.example.semproject.model.ProductRepositoryImpl
import com.example.semproject.viewmodel.ProductViewModel

class DashBoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashBoardBinding

    lateinit var productViewModel: ProductViewModel

    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        adapter = ProductAdapter(this@DashBoardActivity,ArrayList())
        productViewModel.getAllProducts()

        productViewModel.allproducts.observe(this){
            it?.let {
                adapter.updateData(it)
            }
        }


        productViewModel.loading.observe(this){loading->
            if(loading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.recycleView.adapter = adapter

        binding.recycleView.layoutManager = LinearLayoutManager (this)

        binding.floatingBtn.setOnClickListener{
            var intent= Intent(this@DashBoardActivity,
                AddProductActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id=adapter.getProductId(viewHolder.adapterPosition)
                productViewModel.deleteProduct(id){
                        success, message->
                    if(success){
                        Toast.makeText(this@DashBoardActivity, message, Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this@DashBoardActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }).attachToRecyclerView(binding.recycleView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}