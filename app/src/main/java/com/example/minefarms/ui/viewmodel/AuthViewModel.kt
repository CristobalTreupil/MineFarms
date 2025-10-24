package com.example.minefarms.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minefarms.data.database.AppDatabase
import com.example.minefarms.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val username: String? = null,
    val displayName: String? = null,
    val avatarEmoji: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val authRepository = AuthRepository(
        userDao = database.userDao(),
        userManager = com.example.minefarms.data.manager.UserManager(application)
    )
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        checkCurrentUser()
    }
    
    private fun checkCurrentUser() {
        viewModelScope.launch {
            authRepository.currentUserId.collect { userId ->
                if (userId != null) {
                    // Cargar datos del usuario
                    authRepository.getUserById(userId).collect { user ->
                        _authState.value = AuthState(
                            isLoggedIn = user != null,
                            userId = user?.id,
                            username = user?.username,
                            displayName = user?.displayName,
                            avatarEmoji = user?.avatarEmoji
                        )
                    }
                } else {
                    _authState.value = AuthState(isLoggedIn = false)
                }
            }
        }
    }
    
    fun login(username: String, password: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            val result = authRepository.login(username, password)
            
            result.fold(
                onSuccess = {
                    _authState.value = _authState.value.copy(isLoading = false)
                    checkCurrentUser()
                    onResult(Result.success(Unit))
                },
                onFailure = { error ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    onResult(Result.failure(error))
                }
            )
        }
    }
    
    fun register(
        username: String,
        email: String,
        password: String,
        displayName: String,
        avatarEmoji: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            val result = authRepository.register(username, email, password, displayName, avatarEmoji)
            
            result.fold(
                onSuccess = {
                    _authState.value = _authState.value.copy(isLoading = false)
                    checkCurrentUser()
                    onResult(Result.success(Unit))
                },
                onFailure = { error ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    onResult(Result.failure(error))
                }
            )
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState(isLoggedIn = false)
        }
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
