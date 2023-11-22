package com.dicoding.metalgotest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.metalgotest.data.local.AuthPreferences
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.data.model.LoginResult
import com.dicoding.metalgotest.data.model.RegisterRequest
import com.dicoding.metalgotest.repository.Repository
import kotlinx.coroutines.launch
import java.io.File

class AuthViewModel(private val pref : AuthPreferences, private val repository: Repository) : ViewModel() {

    fun register(file: File, data: RegisterRequest) = repository.register(file, data)

    fun login(data: LoginRequest) = repository.login(data)

    fun getAuth(): LiveData<LoginResult> {
        return pref.getAuth().asLiveData()
    }
    fun setAuth(data: LoginResult) {
        viewModelScope.launch {
            pref.setAuth(data)
        }
    }

    companion object{
        private const val TAG = "AuthViewModel"
    }
}