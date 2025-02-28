package com.example.semproject.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semproject.R
import com.example.semproject.adapter.ProductAdapter
import com.example.semproject.databinding.ActivityDashBoardBinding
import com.example.semproject.repository.ProductRepositoryImpl
import com.example.semproject.ui.fragment.HomeFragment
import com.example.semproject.ui.fragment.ProfileFragment
import com.example.semproject.ui.fragment.cartFragment
import com.example.semproject.viewmodel.ProductViewModel

class DashBoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Only add the default fragment if this is the first creation
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeId -> replaceFragment(HomeFragment())
                R.id.profileId -> replaceFragment(ProfileFragment())
                R.id.cartId-> replaceFragment(cartFragment())
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        try {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(binding.frameLayout.id, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}