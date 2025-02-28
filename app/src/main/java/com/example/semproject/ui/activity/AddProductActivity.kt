package com.example.semproject.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.semproject.R
import com.example.semproject.databinding.ActivityAddProductBinding
import com.example.semproject.model.ProductModel
import com.example.semproject.model.ProductRepositoryImpl
import com.example.semproject.viewmodel.ProductViewModel

class AddProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProductBinding

    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)


        binding.addBtn.setOnClickListener {
            var productName = binding.editProductName.text.toString()
            var productPrice = binding.editProductPrice.text.toString().toInt()
            var productDesc = binding.editProductDesc.text.toString()

            var model = ProductModel(
                "", productName,
                productDesc, productPrice
            )

            productViewModel.addProduct(model) { success, message ->
                if (success) {
                    Toast.makeText(
                        this@AddProductActivity,
                        message, Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@AddProductActivity,
                        message, Toast.LENGTH_LONG
                    ).show()
                }
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}