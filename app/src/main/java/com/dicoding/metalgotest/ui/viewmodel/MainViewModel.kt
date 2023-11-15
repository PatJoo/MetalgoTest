package com.dicoding.metalgotest.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.metalgotest.data.local.AuthPreferences
import com.dicoding.metalgotest.data.model.LoginResult
import com.dicoding.metalgotest.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val pref: AuthPreferences, private val repository: Repository) :
        ViewModel() {
            companion object{
                private const val TAG = "MainViewModel"
            }
        fun getAuth(): LiveData<LoginResult>{
            return pref.getAuth().asLiveData()
        }
        fun getListUser(token: String?) = repository.getUserList(token)

        fun clearSession(){
            viewModelScope.launch{
                pref.clearSession()
            }
        }
}