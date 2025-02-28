package com.example.semproject.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.semproject.R
import com.example.semproject.adapter.ProductAdapter
import com.example.semproject.databinding.ActivityUpdateProductBinding
import com.example.semproject.model.ProductRepositoryImpl
import com.example.semproject.viewmodel.ProductViewModel

class UpdateProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateProductBinding

    lateinit var productViewModel: ProductViewModel

    lateinit var adapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        val id: String = intent.getStringExtra("productId").toString()
        productViewModel.getProductById(id)

        productViewModel.products.observe(this){
            binding.editProductNameid.setText(it?.productName.toString())
            binding.editProductPriceid.setText(it?.price.toString())
            binding.editProductDescid.setText(it?.productDesc.toString())
        }

        binding.btnUpdateProduct.setOnClickListener{
            val newProductName = binding.editProductNameid.text.toString()
            val newProductPrice = binding.editProductPriceid.text.toString().toInt()
            val newProductDesc = binding.editProductDescid.text.toString()

            var updatedMap = mutableMapOf<String,Any>()

            updatedMap["ProductName"] = newProductName
            updatedMap["Price"] = newProductPrice
            updatedMap["ProductDesc"] = newProductDesc

            productViewModel.updateProduct(id,updatedMap) {success,message->
                if(success){
                    Toast.makeText(this@UpdateProductActivity, message, Toast.LENGTH_LONG).show()
                    finish()

                }else{
                    Toast.makeText(this@UpdateProductActivity,message, Toast.LENGTH_LONG).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}