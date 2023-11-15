package com.dicoding.metalgotest.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val first_name : String,
    val last_name : String,
    val gender : Boolean,
    @SerializedName("date_of_birth")
    val dob : String,
    val email : String,
    val phone : String,
    val address : String,
    val photo : Bitmap,
    val password : String

)
