package com.example.semproject.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.semproject.R
import com.example.semproject.databinding.ActivityLoginBinding
import com.example.semproject.repository.UserRepositoryImpl
import com.example.semproject.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        binding.btnLogin.setOnClickListener{
            var email = binding.editEmail.text.toString()
            var password = binding.editPassword.text.toString()


            userViewModel.login(email,password) { success, message ->
                if (success) {
                    var intent = Intent(this@LoginActivity,IntroActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        message, Toast.LENGTH_LONG
                    ).show()

                }
            }
        }

        binding.btnSignupNavigate.setOnClickListener{
            val intent = Intent(this@LoginActivity,
                SignupActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}