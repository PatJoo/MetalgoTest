package com.dicoding.metalgotest.data.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("iserror")
	val iserror: Boolean,

	@field:SerializedName("status")
	val status: Int
)

data class Data(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)
