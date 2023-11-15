package com.dicoding.metalgotest.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.metalgotest.data.local.AuthPreferences
import com.dicoding.metalgotest.repository.Repository

object Injection {
    fun provideAuthPref(dataStore: DataStore<Preferences>): AuthPreferences {
        return AuthPreferences.getInstance(dataStore)
    }

    fun provideRepository(): Repository {
        return Repository()
    }
}