package com.dicoding.metalgotest.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.metalgotest.data.model.Data
import com.dicoding.metalgotest.data.model.DataItem
import com.dicoding.metalgotest.databinding.ActivityMainBinding
import com.dicoding.metalgotest.ui.ViewModelFactory
import com.dicoding.metalgotest.ui.adapter.ListUserAdapter
import com.dicoding.metalgotest.ui.viewmodel.MainViewModel
import com.dicoding.metalgotest.utils.Response
import com.dicoding.metalgotest.utils.dataStore

class MainActivity : AppCompatActivity() {
    companion object{
        private val TAG ="MainActivity"
    }

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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListUser() {
        MainViewModel.getAuth().observe(this) { user ->
            if (user.token != "null") {
                MainViewModel.getListUser("Bearer ${user.token}").observe(this) { response ->
                    when (response) {
                        is Response.Success -> {
                            setUserData(response.data.listUser!!)
                        }
                        is Response.Error -> {
                            Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                        }
                        is Response.Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun setUserData(listUser: List<DataItem>) {
        val adapter = ListUserAdapter(listUser)
        binding.rvItem.adapter = adapter
    }

}