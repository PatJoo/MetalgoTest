package com.dicoding.metalgotest.data.remote

import com.dicoding.metalgotest.data.model.GetDetailUserResponse
import com.dicoding.metalgotest.data.model.GetUserListResponse
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.data.model.LoginResponse
import com.dicoding.metalgotest.data.model.RegisterRequest
import com.dicoding.metalgotest.data.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body data : RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body data : LoginRequest
    ): LoginResponse

    @GET("admin")
    suspend fun getUserList(
        @Header("Authorization") Authorization: String? = null,
    ): GetUserListResponse

    @GET("admin/:id")
    suspend fun getDetailUser(
        @Header("Authorization") Authorization: String? = null,
    ): GetDetailUserResponse

}