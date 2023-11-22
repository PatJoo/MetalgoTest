package com.dicoding.metalgotest.data.remote

import com.dicoding.metalgotest.data.model.GetDetailUserResponse
import com.dicoding.metalgotest.data.model.GetUserResponse
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.data.model.LoginResponse
import com.dicoding.metalgotest.data.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("auth/register")
    suspend fun register(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body data : LoginRequest
    ): LoginResponse

    @GET("admin")
    suspend fun getUserList(
        @Header("Authorization") Authorization: String? = null,
    ): GetUserResponse

    @GET("admin/{id}")
    suspend fun getDetailUser(
        @Header("Authorization") Authorization: String? = null,
        @Path("id") id : String
    ): GetDetailUserResponse

}