package com.dicoding.metalgotest.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dicoding.metalgotest.data.model.DataUser
import com.dicoding.metalgotest.databinding.FragmentUserDetailBinding
import com.dicoding.metalgotest.ui.ViewModelFactory
import com.dicoding.metalgotest.ui.viewmodel.DetailUserViewModel
import com.dicoding.metalgotest.utils.Response
import com.dicoding.metalgotest.utils.dataStore
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserDetail : DialogFragment() {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val detailUserViewModel: DetailUserViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("ID", "")

        getDataUser(id!!)

        binding.btnClose.setOnClickListener{
            onDestroyView()
        }

        binding.btnEdit.setOnClickListener{
            val intent = Intent(requireContext(), EditUserActivity::class.java)
            intent.putExtra(EditUserActivity.EXTRA_ID, id)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataUser(id: String) {
        detailUserViewModel.getAuth().observe(this) { user ->
            if (user.token != "null") {
                showLoading(true)
                detailUserViewModel.getDetailUser("Bearer ${user.token}", id)
                    .observe(this) { response ->
                        when (response) {
                            is Response.Success -> {
                                showLoading(false)
                                setUserData(response.data.data)
                            }

                            is Response.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    requireContext(),
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
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                onDestroyView()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUserData(data: DataUser) {
        binding.tvName.text = data.firstName.plus(" ").plus(data.lastName)
        binding.tvEmail.text = data.email
        binding.tvGender.text = data.gender
        binding.tvPhone.text = data.phone
        binding.tvBirth.text = data.dateOfBirth
        binding.tvAddress.text = data.address

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = LocalDate.parse(data.dateOfBirth, formatter)
        binding.tvBirth.text = "${date.dayOfMonth} ${date.month.toString().lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"

        val imageBytes = Base64.decode(data.photo, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        decodedImage.compress(Bitmap.CompressFormat.JPEG, 10, ByteArrayOutputStream())
        binding.ivUserPhoto.setImageBitmap(decodedImage)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "UserDetail"
        const val KEY = "key"
    }
}


