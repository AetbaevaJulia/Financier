package com.example.financier.presenter.viewModels

import android.content.SharedPreferences
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

    fun init() { //TODO требует доработки (проверка токена)
        if (isLoggedIn()) {
            viewModelScope.launch {
                _authState.value = AuthState.Success(
                    AuthResponse(
                        getUserId().toString(),
                        getToken().toString()
                    )
                )
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = registerUseCase(email, password)

            result.onSuccess { response ->
                saveAuthData(response)
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
                _authState.value = AuthState.Success(response)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Ошибка входа")
            }
        }
    }

    private fun saveAuthData(response: AuthResponse) {
        sharedPreferences.edit().apply {
            putString("auth_token", response.token)
            putString("user_id", response.user_id)
            apply()
        }
    }

    fun getToken(): String? = sharedPreferences.getString("auth_token", null)

    fun getUserId(): String? = sharedPreferences.getString("user_id", null)

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