package com.dicoding.metalgotest.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.metalgotest.data.model.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        const val EMAIL = "email"
        const val NAME = "name"
        const val TOKEN = "token"

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreferences {
            return AuthPreferences(dataStore)
        }
    }

    suspend fun setAuth(data: LoginResult) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(EMAIL)] = data.email
            preferences[stringPreferencesKey(NAME)] = data.name
            preferences[stringPreferencesKey(TOKEN)] = data.token
        }
    }

    fun getAuth(): Flow<LoginResult> {
        return dataStore.data.map { preferences ->
            LoginResult(
                preferences[stringPreferencesKey(EMAIL)].toString(),
                preferences[stringPreferencesKey(NAME)].toString(),
                preferences[stringPreferencesKey(TOKEN)].toString()
            )
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}