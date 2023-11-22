package com.dicoding.metalgotest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.metalgotest.data.model.GetDetailUserResponse
import com.dicoding.metalgotest.data.model.GetUserResponse
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.data.model.LoginResponse
import com.dicoding.metalgotest.data.model.RegisterRequest
import com.dicoding.metalgotest.data.model.RegisterResponse
import com.dicoding.metalgotest.data.remote.ApiService
import com.dicoding.metalgotest.utils.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repository (private val apiService: ApiService) {

    fun register(
        file: File,
        data: RegisterRequest
    ): LiveData<Response<RegisterResponse>> = liveData {
        emit(Response.Loading)
        try {
            val requestBody = HashMap<String, RequestBody>()
            requestBody["first_name"] = data.first_name.toRequestBody("text/plain".toMediaType())
            requestBody["last_name"] = data.last_name.toRequestBody("text/plain".toMediaType())
            requestBody["gender"] = data.gender.toRequestBody("text/plain".toMediaType())
            requestBody["date_of_birth"] = data.dob.toRequestBody("text/plain".toMediaType())
            requestBody["email"] = data.email.toRequestBody("text/plain".toMediaType())
            requestBody["phone"] = data.phone.toRequestBody("text/plain".toMediaType())
            requestBody["address"] = data.address.toRequestBody("text/plain".toMediaType())
            requestBody["password"] = data.password.toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile,
            )

            val response = apiService
                .register(requestBody, imageMultipart)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
            Log.d(TAG, "register: ${e.message.toString()}")
        }
    }

    fun login(data: LoginRequest): LiveData<Response<LoginResponse>> = liveData {
        emit(Response.Loading)
        try {
            val response = apiService
                .login(data)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
            Log.d(TAG, "login: ${e.message.toString()}")
        }
    }

    fun getUserList(token: String?): LiveData<Response<GetUserResponse>> = liveData {
        emit(Response.Loading)
        try {
            val response = apiService
                .getUserList(token)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
        }
    }

    fun getUserDetail(token: String?, id : String): LiveData<Response<GetDetailUserResponse>> = liveData {
            emit(Response.Loading)
            try {
                val response = apiService
                    .getDetailUser(token, id)
                emit(Response.Success(response))
            } catch (e: Exception) {
                emit(Response.Error(e.message.toString()))
            }
        }



    companion object {
        const val TAG = "Repository"
    }
}