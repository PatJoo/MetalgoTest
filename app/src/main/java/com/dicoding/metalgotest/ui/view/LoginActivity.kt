package com.dicoding.metalgotest.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edtEmail.error = "Masukkan email"
                    Toast.makeText(this@LoginActivity, "Masukan email", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = "Masukkan password"
                    Toast.makeText(this@LoginActivity, "Masukkan password", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val data = LoginRequest(
                        binding.edtEmail.text.toString(),
                        binding.edtPassword.text.toString().sha256()
                    )
                    login(data)
                }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}