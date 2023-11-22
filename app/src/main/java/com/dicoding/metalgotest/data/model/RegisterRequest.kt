package com.dicoding.metalgotest.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val first_name : String,
    val last_name : String,
    val gender : String,
    val address : String,
    @SerializedName("date_of_birth")
    val dob : String,
    val email : String,
    val phone : String,
    val password : String

)
