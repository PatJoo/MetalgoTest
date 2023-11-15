package com.dicoding.metalgotest.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.metalgotest.data.local.AuthPreferences
import com.dicoding.metalgotest.di.Injection
import com.dicoding.metalgotest.repository.Repository
import com.dicoding.metalgotest.ui.viewmodel.AuthViewModel
import com.dicoding.metalgotest.ui.viewmodel.MainViewModel

class ViewModelFactory private constructor(private val pref: AuthPreferences, private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(pref, repository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pref, repository)as T
        }
        return super.create(modelClass)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(dataStore: DataStore<Preferences>): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideAuthPref(dataStore), Injection.provideRepository())
            }.also { instance = it }
    }
}