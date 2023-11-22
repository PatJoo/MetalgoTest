package com.dicoding.metalgotest.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.metalgotest.data.model.DataUser
import com.dicoding.metalgotest.databinding.ActivityUpdateUserBinding
import com.dicoding.metalgotest.ui.ViewModelFactory
import com.dicoding.metalgotest.ui.viewmodel.DetailUserViewModel
import com.dicoding.metalgotest.utils.Response
import com.dicoding.metalgotest.utils.dataStore
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var binding :ActivityUpdateUserBinding
    private val editUserViewModel : DetailUserViewModel  by viewModels {
        ViewModelFactory.getInstance(dataStore)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        getDataUser(id!!)

        binding.tlDate.setEndIconOnClickListener{
            getDate()
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDataUser(data: DataUser) {
        binding.edtName.setText(data.firstName.plus(" ").plus(data.lastName))
        binding.edtEmail.setText(data.email)
        binding.edtPhone.setText(data.phone)
        binding.edtAddress.setText(data.address)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = LocalDate.parse(data.dateOfBirth, formatter)
        binding.edtDate.setText(
            "${date.dayOfMonth} ${
                date.month.toString().lowercase().replaceFirstChar { it.uppercase() }
            } ${date.year}"
        )

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataUser(id: String) {
        editUserViewModel.getAuth().observe(this) { user ->
            if (user.token != "null") {
                showLoading(true)
                editUserViewModel.getDetailUser("Bearer ${user.token}", id)
                    .observe(this) { response ->
                        when (response) {
                            is Response.Success -> {
                                showLoading(false)
                                setDataUser(response.data.data)
                            }

                            is Response.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    this,
                                    "Problem Occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Response.Loading -> {
                                showLoading(true)
                            }

                            else -> {}
                        }
                    }
            } else {
                val intent = Intent(this@UpdateUserActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getDate() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(supportFragmentManager, "date")

        datePicker.addOnPositiveButtonClickListener {
            val time = datePicker.headerText
            binding.edtDate.setText(time)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "EditUserActivity"
        const val EXTRA_ID = "extra_id"
    }
}