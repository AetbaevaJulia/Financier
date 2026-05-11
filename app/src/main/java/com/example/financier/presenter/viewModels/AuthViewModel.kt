package com.example.financier.presenter.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financier.data.model.AuthResponse
import com.example.financier.domain.authUseCases.LoginUseCase
import com.example.financier.domain.authUseCases.RegisterUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = registerUseCase(email, password)

            result.onSuccess { response ->
                saveToken(response.token)
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
                saveToken(response.token)
                _authState.value = AuthState.Success(response)
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Ошибка регистрации")
            }
        }
    }

    private fun saveToken(token: String) {
        // TODO: Сохранить в SharedPreferences / DataStore
        // prefs.edit().putString("auth_token", token).apply()
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val response: AuthResponse) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}