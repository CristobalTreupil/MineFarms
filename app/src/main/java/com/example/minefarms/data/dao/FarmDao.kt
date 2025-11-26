package com.example.minefarms.data.dao

import androidx.room.*
import com.example.minefarms.data.entity.FarmEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones de granjas en la base de datos
 * Implementa persistencia interna con Room según requisitos
 */
@Dao
interface FarmDao {
    
    /**
     * Obtiene todas las granjas ordenadas por nombre
     */
    @Query("SELECT * FROM farms ORDER BY name ASC")
    fun getAllFarms(): Flow<List<FarmEntity>>
    
    /**
     * Obtiene una granja específica por ID
     */
    @Query("SELECT * FROM farms WHERE id = :farmId")
    fun getFarmById(farmId: Long): Flow<FarmEntity?>
    
    /**
     * Obtiene granjas por nivel de dificultad
     */
    @Query("SELECT * FROM farms WHERE difficulty = :difficulty ORDER BY name ASC")
    fun getFarmsByDifficulty(difficulty: String): Flow<List<FarmEntity>>
    
    /**
     * Busca granjas por nombre o descripción
     */
    @Query("SELECT * FROM farms WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFarms(query: String): Flow<List<FarmEntity>>
    
    /**
     * Obtiene granjas creadas por un usuario específico
     */
    @Query("SELECT * FROM farms WHERE createdBy = :userId ORDER BY name ASC")
    fun getFarmsByUser(userId: Long): Flow<List<FarmEntity>>
    
    /**
     * Obtiene solo granjas personalizadas del usuario
     */
    @Query("SELECT * FROM farms WHERE isCustom = 1 AND createdBy = :userId ORDER BY name ASC")
    fun getCustomFarmsByUser(userId: Long): Flow<List<FarmEntity>>
    
    /**
     * Inserta una nueva granja
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: FarmEntity): Long
    
    /**
     * Inserta múltiples granjas
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarms(farms: List<FarmEntity>)
    
    /**
     * Actualiza una granja existente
     */
    @Update
    suspend fun updateFarm(farm: FarmEntity)
    
    /**
     * Elimina una granja
     */
    @Delete
    suspend fun deleteFarm(farm: FarmEntity)
    
    /**
     * Elimina una granja por ID
     */
    @Query("DELETE FROM farms WHERE id = :farmId")
    suspend fun deleteFarmById(farmId: Long)
    
    /**
     * Elimina todas las granjas
     */
    @Query("DELETE FROM farms")
    suspend fun deleteAllFarms()
    
    /**
     * Cuenta el total de granjas
     */
    @Query("SELECT COUNT(*) FROM farms")
    suspend fun getFarmsCount(): Int
}
