package com.example.minefarms.data.dao

import androidx.room.*
import com.example.minefarms.data.entity.UserFarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFarmDao {
    @Query("SELECT * FROM user_farms WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserFarms(userId: Long): Flow<List<UserFarmEntity>>
    
    @Query("SELECT * FROM user_farms WHERE id = :farmId")
    fun getFarmById(farmId: Long): Flow<UserFarmEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: UserFarmEntity): Long
    
    @Update
    suspend fun updateFarm(farm: UserFarmEntity)
    
    @Delete
    suspend fun deleteFarm(farm: UserFarmEntity)
    
    @Query("DELETE FROM user_farms WHERE id = :farmId")
    suspend fun deleteFarmById(farmId: Long)
}
