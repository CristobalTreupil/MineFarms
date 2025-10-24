package com.example.minefarms.data.dao

import androidx.room.*
import com.example.minefarms.data.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun getFavoritesByUser(userId: Long): Flow<List<FavoriteEntity>>
    
    @Query("SELECT * FROM favorites WHERE userId = :userId AND isLiked = 1")
    fun getLikedFarms(userId: Long): Flow<List<FavoriteEntity>>
    
    @Query("SELECT * FROM favorites WHERE userId = :userId AND isSaved = 1")
    fun getSavedFarms(userId: Long): Flow<List<FavoriteEntity>>
    
    @Query("SELECT farmId FROM favorites WHERE userId = :userId")
    fun getFavoriteFarmIds(userId: Long): Flow<List<Int>>
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND farmId = :farmId)")
    fun isFavorite(userId: Long, farmId: Int): Flow<Boolean>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)
    
    @Query("UPDATE favorites SET isLiked = :isLiked WHERE userId = :userId AND farmId = :farmId")
    suspend fun updateLiked(userId: Long, farmId: Int, isLiked: Boolean)
    
    @Query("UPDATE favorites SET isSaved = :isSaved WHERE userId = :userId AND farmId = :farmId")
    suspend fun updateSaved(userId: Long, farmId: Int, isSaved: Boolean)
    
    @Query("DELETE FROM favorites WHERE userId = :userId AND farmId = :farmId")
    suspend fun deleteFavorite(userId: Long, farmId: Int)
    
    @Delete
    suspend fun deleteFavoriteEntity(favorite: FavoriteEntity)
}
