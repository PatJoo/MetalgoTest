package com.dicoding.metalgotest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.metalgotest.data.local.AuthPreferences
import com.dicoding.metalgotest.data.model.LoginRequest
import com.dicoding.metalgotest.data.model.LoginResult
import com.dicoding.metalgotest.repository.Repository

class DetailUserViewModel(private val pref : AuthPreferences, private val repository: Repository) :
    ViewModel(){
        fun getDetailUser(token:String?) = repository.getUserDetail(token)

    fun getAuth(): LiveData<LoginResult>{
        return pref.getAuth().asLiveData()
    }
}