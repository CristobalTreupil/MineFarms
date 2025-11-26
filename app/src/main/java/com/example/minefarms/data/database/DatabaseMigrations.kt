package com.example.minefarms.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migraciones de la base de datos Room
 * Actualmente se usa fallbackToDestructiveMigration() en AppDatabase
 * Este archivo está preparado para futuras migraciones sin perder datos
 */
object DatabaseMigrations {
    
    /**
     * Migración de versión 3 a 4
     * Agrega la tabla farms para persistencia de granjas
     */
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Crear tabla farms
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS farms (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    materials TEXT NOT NULL,
                    difficulty TEXT NOT NULL,
                    production TEXT NOT NULL,
                    productionRate TEXT NOT NULL,
                    process TEXT NOT NULL,
                    tutorialUrl TEXT NOT NULL,
                    imageResourceName TEXT NOT NULL,
                    tags TEXT NOT NULL,
                    isCustom INTEGER NOT NULL DEFAULT 0,
                    createdBy INTEGER
                )
            """.trimIndent())
        }
    }
    
    /**
     * Lista de todas las migraciones disponibles
     * Agregar aquí nuevas migraciones en el futuro
     */
    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_3_4
    )
}
