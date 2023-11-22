package com.dicoding.metalgotest.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.dicoding.metalgotest.data.model.RegisterRequest
import com.dicoding.metalgotest.databinding.ActivityAddUserBinding
import com.dicoding.metalgotest.ui.ViewModelFactory
import com.dicoding.metalgotest.ui.viewmodel.AuthViewModel
import com.dicoding.metalgotest.utils.Response
import com.dicoding.metalgotest.utils.dataStore
import com.dicoding.metalgotest.utils.reduceFileImage
import com.dicoding.metalgotest.utils.sha256
import com.dicoding.metalgotest.utils.toIsoString
import com.dicoding.metalgotest.utils.uriToFile
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class AddUser : AppCompatActivity() {

    private lateinit var binding : ActivityAddUserBinding
    private val authViewModel : AuthViewModel by viewModels{
        ViewModelFactory.getInstance(dataStore)
    }

    private var getFile: File? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tilDate.setEndIconOnClickListener {
            getDate()
        }

        binding.tilPhoto.setEndIconOnClickListener {
            startGallery()
        }

        binding.btnAddUser.setOnClickListener {
            if (isValidRegister()) {
                val fullName = binding.edtName.text.toString()

                val gender = when (binding.rgGender.checkedRadioButtonId) {
                    binding.rbMale.id -> "male"
                    binding.rbFemale.id -> "female"
                    else -> ""
                }

                val email = binding.edtEmail.text.toString()
                val phone = binding.edtPhone.text.toString()
                val address = binding.edtAddress.text.toString()
                val password = binding.edtPassword.text.toString()

                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
                val current = LocalDate.parse(binding.edtDate.text?.trim(), formatter)
                val dob = current.toIsoString()

                val data = RegisterRequest(
                    fullName,
                    fullName,
                    gender,
                    address,
                    dob,
                    email,
                    phone,
                    password.sha256()
                )
                register(data)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isValidRegister(): Boolean {
        val fullName = binding.edtName.text.toString()
        val gender = when (binding.rgGender.checkedRadioButtonId) {
            binding.rbMale.id -> "male"
            binding.rbFemale.id -> "female"
            else -> ""
        }

        val email = binding.edtEmail.text.toString()
        val phone = binding.edtPhone.text.toString()
        val address = binding.edtAddress.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfPassword.text.toString()
        val dob = binding.edtDate.text.toString()
        val photo = binding.edtPhoto.text.toString()

        when {
            fullName.isEmpty() -> Toast.makeText(
                this@AddUser,
                "Name Required",
                Toast.LENGTH_SHORT
            ).show()

            gender.isEmpty() -> Toast.makeText(
                this@AddUser,
                "Choose Your Gender",
                Toast.LENGTH_SHORT
            ).show()

            dob.isEmpty() -> Toast.makeText(
                this@AddUser,
                "Please Fill With Your Birth Date",
                Toast.LENGTH_SHORT
            ).show()

            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> Toast.makeText(
                this@AddUser,
                "Wrong Email Format",
                Toast.LENGTH_SHORT
            ).show()

            address.isEmpty() -> Toast.makeText(
                this@AddUser,
                "Address Required!",
                Toast.LENGTH_SHORT
            ).show()

            phone.isEmpty() -> Toast.makeText(
                this@AddUser,
                "Name Required",
                Toast.LENGTH_SHORT
            ).show()

            photo.isEmpty() -> Toast.makeText(
                this@AddUser,
                "Photo Required",
                Toast.LENGTH_SHORT
            ).show()

            password.isEmpty() || password.length < 8 || !password.any { it.isDigit() } || !password.any { it.isLetter() } || password == password.lowercase() -> Toast.makeText(
                this@AddUser,
                "Password Wrong Format",
                Toast.LENGTH_SHORT
            ).show()

            confirmPassword != password -> Toast.makeText(
                this@AddUser,
                "Password Not Same",
                Toast.LENGTH_SHORT
            ).show()

            else -> return true
        }
        return false
    }

    private fun register(data: RegisterRequest) {
        val file = reduceFileImage(getFile as File)
        showLoading(true)
        authViewModel.register(file, data).observe(this) { response ->
            when (response) {
                is Response.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Response.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Problem Occurred", Toast.LENGTH_SHORT).show()
                }

                is Response.Loading -> {
                    showLoading(true)
                }
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

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            Log.d(TAG, "tess: ${result.data!!.data}}")

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddUser)
                getFile = myFile
                binding.edtPhoto.setText(
                    DocumentFile.fromSingleUri(
                        this@AddUser,
                        uri
                    )!!.name
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}