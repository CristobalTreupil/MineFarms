package com.example.minefarms.repository

import com.example.minefarms.data.dao.FarmDao
import com.example.minefarms.data.entity.FarmEntity
import com.example.minefarms.model.Farm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para manejar operaciones de granjas
 * Implementa persistencia interna con Room según arquitectura MVVM
 */
class FarmRepository(private val farmDao: FarmDao) {
    
    private val gson = Gson()
    
    /**
     * Obtiene todas las granjas como Flow
     */
    fun getAllFarms(): Flow<List<Farm>> {
        return farmDao.getAllFarms().map { entities ->
            entities.map { it.toFarm() }
        }
    }
    
    /**
     * Obtiene una granja por ID
     */
    fun getFarmById(farmId: Long): Flow<Farm?> {
        return farmDao.getFarmById(farmId).map { it?.toFarm() }
    }
    
    /**
     * Busca granjas por query
     */
    fun searchFarms(query: String): Flow<List<Farm>> {
        return farmDao.searchFarms(query).map { entities ->
            entities.map { it.toFarm() }
        }
    }
    
    /**
     * Inserta una nueva granja
     */
    suspend fun insertFarm(farm: Farm): Long {
        return farmDao.insertFarm(farm.toEntity())
    }
    
    /**
     * Actualiza una granja existente
     */
    suspend fun updateFarm(farm: Farm) {
        farmDao.updateFarm(farm.toEntity())
    }
    
    /**
     * Elimina una granja
     */
    suspend fun deleteFarm(farm: Farm) {
        farmDao.deleteFarmById(farm.id)
    }
    
    /**
     * Inicializa la base de datos con granjas predefinidas
     */
    suspend fun initializeDefaultFarms() {
        val count = farmDao.getFarmsCount()
        if (count == 0) {
            val defaultFarms = getDefaultFarms()
            farmDao.insertFarms(defaultFarms.map { it.toEntity() })
        }
    }
    
    /**
     * Convierte FarmEntity a Farm (modelo de UI)
     */
    private fun FarmEntity.toFarm(): Farm {
        val materialsType = object : TypeToken<List<String>>() {}.type
        val tagsType = object : TypeToken<List<String>>() {}.type
        
        return Farm(
            id = this.id,
            name = this.name,
            description = this.description,
            materials = gson.fromJson(this.materials, materialsType),
            difficulty = this.difficulty,
            production = this.production,
            productionRate = this.productionRate,
            process = this.process,
            tutorialUrl = this.tutorialUrl,
            imageResourceName = this.imageResourceName,
            tags = gson.fromJson(this.tags, tagsType),
            imageUri = this.imageUri
        )
    }
    
    /**
     * Convierte Farm a FarmEntity
     */
    private fun Farm.toEntity(): FarmEntity {
        return FarmEntity(
            id = 0, // Dejar que Room genere el ID automáticamente
            name = this.name,
            description = this.description,
            materials = gson.toJson(this.materials),
            difficulty = this.difficulty,
            production = this.production,
            productionRate = this.productionRate,
            process = this.process,
            tutorialUrl = this.tutorialUrl,
            imageResourceName = this.imageResourceName,
            tags = gson.toJson(this.tags),
            imageUri = this.imageUri
        )
    }
    
    /**
     * Lista de granjas predefinidas
     */
    private fun getDefaultFarms(): List<Farm> {
        return listOf(
            Farm(
                id = 1,
                name = "Granja de Hierro con Aldeanos",
                description = "Una granja automatizada que usa aldeanos y zombis para generar gólems de hierro continuamente.",
                materials = listOf(
                    "20 Aldeanos",
                    "3 Zombis",
                    "Bloques sólidos (100+)",
                    "Camas (20)",
                    "Tolvas (10-20)",
                    "Cofres (5-10)",
                    "Agua o Lava"
                ),
                difficulty = "Media",
                production = "Lingotes de Hierro y Amapolas",
                productionRate = "~200-300 lingotes/hora",
                process = "Los aldeanos detectan al zombi y generan gólems de hierro. Los gólems son empujados hacia una cámara de muerte con lava o caída. Los drops se recolectan automáticamente con tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=KGZEJ-kCC2I",
                imageResourceName = "granja_de_hierro",
                tags = listOf("Recursos", "Automática", "Mob", "Esencial")
            ),
            Farm(
                id = 2,
                name = "Granja de Experiencia con Enderman",
                description = "Una de las mejores granjas de XP en el End, usando Endermans para obtener experiencia y perlas.",
                materials = listOf(
                    "Bloques del End (200+)",
                    "Agua (varios cubos)",
                    "Tolvas (20+)",
                    "Cofres (10)",
                    "Pistones pegajosos (opcional)",
                    "Redstone (50+)"
                ),
                difficulty = "Media-Difícil",
                production = "Experiencia y Perlas de Ender",
                productionRate = "30+ niveles en minutos",
                process = "Los Endermans aparecen en una plataforma y son atraídos hacia un lugar donde el jugador los puede matar de un golpe. Las perlas caen en tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=du0pO9vrPbo",
                imageResourceName = "granja_de_experiencia_con_enderman",
                tags = listOf("Experiencia", "End", "Mob", "Recursos")
            ),
            Farm(
                id = 3,
                name = "Granja de Creepers para Pólvora",
                description = "Granja de mob spawner enfocada en Creepers para obtener pólvora de manera eficiente.",
                materials = listOf(
                    "Bloques sólidos (300+)",
                    "Gatos (2-4)",
                    "Tolvas (15+)",
                    "Cofres (5)",
                    "Trampillas (50+)",
                    "Agua (varios cubos)"
                ),
                difficulty = "Media",
                production = "Pólvora",
                productionRate = "~400 pólvora/hora",
                process = "Los Creepers aparecen en plataformas oscuras y son empujados por gatos hacia un sistema de caída. Mueren por daño de caída y sueltan pólvora recolectada por tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=0wY5ulRReos",
                imageResourceName = "granja_de_creepers_para_polvora",
                tags = listOf("Mob", "Recursos", "Automática", "Overworld")
            ),
            Farm(
                id = 4,
                name = "Granja de Caña de Azúcar",
                description = "Granja totalmente automática de caña de azúcar usando observadores y pistones.",
                materials = listOf(
                    "Observadores (por columna)",
                    "Pistones (por columna)",
                    "Redstone (50+)",
                    "Bloques de tierra/arena",
                    "Caña de azúcar (inicial)",
                    "Tolvas (10+)",
                    "Agua"
                ),
                difficulty = "Fácil",
                production = "Caña de Azúcar",
                productionRate = "Variable (depende del tamaño)",
                process = "Cuando la caña crece a 3 bloques, el observador detecta el cambio y activa un pistón que rompe la caña. Los items caen y son recolectados por tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=r2DWgPje-f8",
                imageResourceName = "granja_de_cana_de_azucar",
                tags = listOf("Recursos", "Automática", "Vegetal", "Redstone")
            ),
            Farm(
                id = 5,
                name = "Granja de Slimes",
                description = "Granja en un chunk de slimes para obtener bolas de slime automáticamente.",
                materials = listOf(
                    "Bloques sólidos (500+)",
                    "Gólems de hierro (4-8)",
                    "Tolvas (20+)",
                    "Cofres (10)",
                    "Lava o magma",
                    "Agua"
                ),
                difficulty = "Media",
                production = "Bolas de Slime",
                productionRate = "~200 bolas/hora",
                process = "Los slimes aparecen en un chunk específico. Los gólems de hierro los atacan y los dividen en slimes pequeños que son eliminados con lava/magma. Los drops caen en tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=btU2ascd_Nc",
                imageResourceName = "granja_de_slime",
                tags = listOf("Mob", "Recursos", "Automática", "Overworld")
            ),
            Farm(
                id = 6,
                name = "Granja de Cultivos Automática",
                description = "Sistema automático para cultivar trigo, zanahorias, papas y remolachas usando aldeanos.",
                materials = listOf(
                    "Aldeanos granjeros (1-4)",
                    "Compostadores",
                    "Bloques de tierra",
                    "Agua",
                    "Tolvas (con trucos)",
                    "Bloques sólidos (100+)"
                ),
                difficulty = "Fácil-Media",
                production = "Trigo, Zanahorias, Papas, Remolachas",
                productionRate = "Varios stacks/hora",
                process = "Los aldeanos granjeros plantan y cosechan automáticamente los cultivos. El excedente puede ser recolectado usando sistemas de tolvas y tolvas-minecarts.",
                tutorialUrl = "https://www.youtube.com/watch?v=hXqEojnO-Fw",
                imageResourceName = "granja_de_cultivos_automatica",
                tags = listOf("Vegetal", "Automática", "Comida", "Aldeanos")
            ),
            Farm(
                id = 7,
                name = "Granja de Wither Esqueletos",
                description = "Granja en el Nether para obtener cráneos de wither esqueleto y carbón.",
                materials = listOf(
                    "Bloques del Nether (500+)",
                    "Lava",
                    "Lobos (opcional)",
                    "Tolvas (30+)",
                    "Cofres (10+)",
                    "Bloques de magma"
                ),
                difficulty = "Difícil",
                production = "Cráneos de Wither Esqueleto, Carbón, Huesos",
                productionRate = "~10-15 cráneos/hora",
                process = "Los wither esqueletos aparecen en una fortaleza del Nether. Son empujados hacia una zona donde mueren por daño continuo. Los cráneos se recolectan con tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=EusTwVA0lQc",
                imageResourceName = "granja_de_wither_esqueletos",
                tags = listOf("Mob", "Nether", "Boss", "Difícil")
            ),
            Farm(
                id = 8,
                name = "Granja de Guardian para Prismarinos",
                description = "Granja en un templo oceánico para obtener prismarinos y experiencia.",
                materials = listOf(
                    "Esponjas (50+)",
                    "Bloques sólidos (1000+)",
                    "Bloques de magma",
                    "Agua",
                    "Tolvas (50+)",
                    "Cofres (20+)"
                ),
                difficulty = "Muy Difícil",
                production = "Cristales de Prismarino, Fragmentos, Pescado",
                productionRate = "Variable (alta producción)",
                process = "Se drena el templo oceánico y se crea una cámara de spawn. Los guardianes son empujados hacia una zona de muerte. Los drops caen en tolvas.",
                tutorialUrl = "https://www.youtube.com/watch?v=Rlp4LOCd9q8",
                imageResourceName = "granja_de_guardianes",
                tags = listOf("Mob", "Océano", "Experiencia", "Muy Difícil")
            )
        )
    }
}