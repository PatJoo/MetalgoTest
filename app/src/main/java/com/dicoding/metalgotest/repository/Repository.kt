package com.dicoding.metalgotest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.metalgotest.data.model.GetDetailUserResponse
import com.dicoding.metalgotest.data.model.GetUserListResponse
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.data.model.LoginResponse
import com.dicoding.metalgotest.data.model.RegisterRequest
import com.dicoding.metalgotest.data.model.RegisterResponse
import com.dicoding.metalgotest.data.remote.ApiConfig
import com.dicoding.metalgotest.utils.Response
import kotlinx.coroutines.flow.collect

class Repository {

    fun register(data: RegisterRequest): LiveData<Response<RegisterResponse>> = liveData {
        emit(Response.Loading)
        try {
            val response = ApiConfig.getApiService()
                .register(data)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
            Log.d(TAG, "regis: ${e.message.toString()}")
        }
    }

    fun login(data: LoginRequest): LiveData<Response<LoginResponse>> = liveData {
        emit(Response.Loading)
        try {
            val response = ApiConfig.getApiService()
                .login(data)
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
            Log.d(TAG, "login: ${e.message.toString()}")
        }
    }

    fun getUserList(token: String?): LiveData<Response<GetUserListResponse>> = liveData {
        emit(Response.Loading)
        try {
            val response = ApiConfig.getApiService(token)
                .getUserList()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
        }
    }

    fun getUserDetail(token: String?): LiveData<Response<GetDetailUserResponse>> = liveData {
            emit(Response.Loading)
            try {
                val response =ApiConfig.getApiService(token)
                    .getDetailUser()
                emit(Response.Success(response))
            } catch (e: Exception) {
                emit(Response.Error(e.message.toString()))
            }
        }

//    fun addNewStory(
//        token: String?,
//        file: File,
//        description: RequestBody
//    ): LiveData<Response<StoryUploadResponse>> = liveData {
//        emit(Response.Loading)
//        try {
//            Log.d(TAG, "addNewStory: tess")
//            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                file.name,
//                requestImageFile
//            )
//            val response = ApiConfig.getApiService(token)
//                .addNewStory(imageMultipart, description)
//            emit(Response.Success(response))
//        } catch (e: Exception) {
//            emit(Response.Error(e.message.toString()))
//        }
//    }

    companion object {
        const val TAG = "Repository"
    }
}