package com.example.minefarms.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.minefarms.data.dao.FarmDao
import com.example.minefarms.data.dao.FavoriteDao
import com.example.minefarms.data.dao.UserDao
import com.example.minefarms.data.dao.UserFarmDao
import com.example.minefarms.data.entity.FarmEntity
import com.example.minefarms.data.entity.FavoriteEntity
import com.example.minefarms.data.entity.UserEntity
import com.example.minefarms.data.entity.UserFarmEntity

@Database(
    entities = [
        UserEntity::class,
        FavoriteEntity::class,
        UserFarmEntity::class,
        FarmEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userFarmDao(): UserFarmDao
    abstract fun farmDao(): FarmDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "minefarms_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
