package com.example.minefarms.data.repository

import com.example.minefarms.data.dao.UserDao
import com.example.minefarms.data.entity.UserEntity
import com.example.minefarms.data.manager.UserManager
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

class AuthRepository(
    private val userDao: UserDao,
    private val userManager: UserManager
) {
    val currentUserId: Flow<Long?> = userManager.currentUserId
    
    fun getUserById(userId: Long): Flow<UserEntity?> {
        return userDao.getUserById(userId)
    }
    
    suspend fun login(username: String, password: String): Result<UserEntity> {
        return try {
            val passwordHash = hashPassword(password)
            val user = userDao.login(username, passwordHash)
            
            if (user != null) {
                userManager.saveUserId(user.id)
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(
        username: String,
        email: String,
        password: String,
        displayName: String,
        avatarEmoji: String = "👤"
    ): Result<UserEntity> {
        return try {
            // Verificar si el usuario ya existe
            if (userDao.getUserByUsername(username) != null) {
                return Result.failure(Exception("El nombre de usuario ya está en uso"))
            }
            
            if (userDao.getUserByEmail(email) != null) {
                return Result.failure(Exception("El email ya está registrado"))
            }
            
            val passwordHash = hashPassword(password)
            val user = UserEntity(
                username = username,
                email = email,
                passwordHash = passwordHash,
                displayName = displayName,
                avatarEmoji = avatarEmoji
            )
            
            val userId = userDao.insertUser(user)
            val insertedUser = user.copy(id = userId)
            userManager.saveUserId(userId)
            
            Result.success(insertedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logout() {
        userManager.clearUserId()
    }
    
    private fun hashPassword(password: String): String {
        // En producción, usa un algoritmo más seguro como BCrypt
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}
