package com.example.financier.presenter.viewModels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.data.model.AuthResponse
import com.example.financier.domain.authUseCases.LoginUseCase
import com.example.financier.domain.authUseCases.RegisterUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit

class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun init() {
        viewModelScope.launch {
            val email = getLastEmail()
            val password = getLastPassword()
            if (email != null || password != null) {
                login(email!!, password!!)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = registerUseCase(email, password)

            result.onSuccess { response ->
                saveAuthData(response)
                saveCredentials(email, password)
                _authState.value = AuthState.Success(response)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Ошибка регистрации")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = loginUseCase(email, password)

            result.onSuccess { response ->
                saveAuthData(response)
                saveCredentials(email, password)
                _authState.value = AuthState.Success(response)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Ошибка входа")
            }
        }
    }

    private fun saveAuthData(response: AuthResponse) {
        sharedPreferences.edit().apply {
            putString("auth_token", "Bearer ${response.token}")
            putString("user_id", response.user_id)
            apply()
        }
    }

    private fun saveCredentials(email: String, password: String) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    fun getToken(): String? = sharedPreferences.getString("auth_token", null)

    fun getUserId(): String? = sharedPreferences.getString("user_id", null)

    fun getLastEmail(): String? = sharedPreferences.getString("email", null)

    fun getLastPassword(): String? = sharedPreferences.getString("password", null)

    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()

    fun logout() {
        sharedPreferences.edit { clear() }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val response: AuthResponse) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}