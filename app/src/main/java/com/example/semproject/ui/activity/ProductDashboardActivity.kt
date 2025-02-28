package com.example.semproject.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.semproject.R
import com.example.semproject.databinding.ActivityProductDashboardBinding
import com.example.semproject.ui.fragment.CartFragment
import com.example.semproject.ui.fragment.HomeFragment
import com.example.semproject.ui.fragment.ProfileFragment

class ProductDashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeId -> replaceFragment(HomeFragment())
                R.id.profileId -> replaceFragment(ProfileFragment())
                R.id.cartId-> replaceFragment(CartFragment())
                else -> {

                }
            }
            true
        };
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentransaction: FragmentTransaction =fragmentManager.beginTransaction()
        fragmentransaction.replace(binding.frameLayout.id,fragment)
        fragmentransaction.commit()

    }
}
}