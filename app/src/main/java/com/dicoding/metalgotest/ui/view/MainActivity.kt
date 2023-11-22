package com.dicoding.metalgotest.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.metalgotest.data.model.DetailListUser
import com.dicoding.metalgotest.databinding.ActivityMainBinding
import com.dicoding.metalgotest.ui.ViewModelFactory
import com.dicoding.metalgotest.ui.adapter.ListUserAdapter
import com.dicoding.metalgotest.ui.viewmodel.MainViewModel
import com.dicoding.metalgotest.utils.Response
import com.dicoding.metalgotest.utils.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val MainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvItem.layoutManager = layoutManager

        getListUser()

        binding.tvLogout.setOnClickListener(){
            MainViewModel.clearSession()
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAddUser.setOnClickListener{
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)
        }

    }

    private fun getListUser() {
        MainViewModel.getAuth().observe(this) { user ->
            if (user.token != "null") {
                showLoading(true)
                MainViewModel.getListUser("Bearer ${user.token}").observe(this) { response ->
                    when (response) {
                        is Response.Success -> {
                            showLoading(false)
                            setUserData(response.data.listUser!!)
                        }
                        is Response.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Problem Occurred", Toast.LENGTH_SHORT).show()
                        }
                        is Response.Loading -> {
                            showLoading(true)
                        }
                        else -> {}
                    }
                }
            }
            else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUserData(listUser: List<DetailListUser>) {
        val adapter = ListUserAdapter(listUser)
        binding.rvItem.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        private val TAG ="MainActivity"
    }

}