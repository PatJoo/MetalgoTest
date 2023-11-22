package com.dicoding.metalgotest.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.databinding.ActivityLoginBinding
import com.dicoding.metalgotest.ui.ViewModelFactory
import com.dicoding.metalgotest.ui.viewmodel.AuthViewModel
import com.dicoding.metalgotest.utils.Response
import com.dicoding.metalgotest.utils.dataStore
import com.dicoding.metalgotest.utils.sha256

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val authViewModel:AuthViewModel by viewModels{
        ViewModelFactory.getInstance(dataStore)
    }

    private var loginValidation = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginChecker()

        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString().sha256()
            when {
                email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.edtEmail.error = "Enter your email correctly!"
                }
                password.isEmpty() || password.length < 8 -> {
                    binding.edtPassword.error = "Enter your password!"
                }
                else -> {
                    val data = LoginRequest(
                        email,password
                    )
                    login(data)
                }
            }

            binding.btnAddUser.setOnClickListener {
                val intent = Intent(this, AddUser::class.java)
                startActivity(intent)
            }
        }
    }

    private fun login(data: LoginRequest) {
        showLoading(true)
        authViewModel.login(data).observe(this) { response ->
            when (response) {
                is Response.Success -> {
                    showLoading(false)
                    authViewModel.setAuth(response.data.LoginResult)
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Response.Error -> {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, "Problem Occurred", Toast.LENGTH_SHORT).show()
                }
                is Response.Loading -> {
                    showLoading(true)
                }

            }
        }
    }

    private fun loginChecker() {
        showLoading(true)
        authViewModel.getAuth().observe(this) { user ->
            if (user.token != "null") {
                showLoading(false)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}