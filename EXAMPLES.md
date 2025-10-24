# Ejemplos para Agregar Más Granjas

## Plantilla Básica

Copia este código en `FarmRepository.kt` y rellena con la información de tu granja:

```kotlin
Farm(
    id = 9, // ⚠️ Usar el siguiente ID disponible
    name = "Nombre de la Granja",
    description = "Descripción corta de la granja (1-2 líneas)",
    materials = listOf(
        "Material 1 (cantidad)",
        "Material 2 (cantidad)",
        "Material 3",
        "..."
    ),
    difficulty = "Fácil", // o "Media", "Difícil", "Muy Difícil"
    production = "Lo que produce la granja",
    productionRate = "Cantidad/hora o descripción",
    process = "Explicación detallada de cómo funciona la granja paso a paso.",
    tutorialUrl = "https://www.youtube.com/watch?v=XXXXXXXXX",
    imageResourceName = "nombre_imagen_sin_extension",
    tags = listOf("Tag1", "Tag2", "Tag3")
)
```

## Ejemplos Completos

### Ejemplo 1: Granja Simple (Bambú)

```kotlin
Farm(
    id = 9,
    name = "Granja de Bambú Automática",
    description = "Granja de bambú usando observadores y pistones para cosecha automática.",
    materials = listOf(
        "Observadores (por columna)",
        "Pistones (por columna)",
        "Redstone (20+)",
        "Bloques de tierra/arena",
        "Bambú inicial",
        "Tolvas (5-10)",
        "Agua"
    ),
    difficulty = "Fácil",
    production = "Bambú",
    productionRate = "Variable según tamaño",
    process = "El bambú crece hasta 3 bloques. Un observador detecta el crecimiento y activa un pistón que rompe el bambú. Los items caen y son recolectados por tolvas conectadas a cofres.",
    tutorialUrl = "https://www.youtube.com/watch?v=ejemplo",
    imageResourceName = "farm_bamboo",
    tags = listOf("Vegetal", "Automática", "Redstone", "Fácil")
)
```

### Ejemplo 2: Granja de Mob (Arañas)

```kotlin
Farm(
    id = 10,
    name = "Granja de Arañas",
    description = "Granja de spawner de arañas para obtener hilo, ojos de araña y experiencia.",
    materials = listOf(
        "Spawner de arañas (encontrado)",
        "Bloques sólidos (100+)",
        "Agua (varios cubos)",
        "Señales o carteles (50+)",
        "Tolvas (10+)",
        "Cofres (5)",
        "Antorchas"
    ),
    difficulty = "Media",
    production = "Hilo, Ojos de Araña, Experiencia",
    productionRate = "~100 hilos/hora",
    process = "Las arañas spawn desde el spawner, son empujadas por agua hacia un pasillo de 1x1 de alto (las arañas no caben). Mueren por daño de sofocación o el jugador las mata. Los drops caen en tolvas.",
    tutorialUrl = "https://www.youtube.com/watch?v=ejemplo",
    imageResourceName = "farm_spider",
    tags = listOf("Mob", "Spawner", "Experiencia", "Overworld")
)
```

### Ejemplo 3: Granja del Nether

```kotlin
Farm(
    id = 11,
    name = "Granja de Magma Cubes",
    description = "Granja en el Nether para obtener crema de magma de forma automática.",
    materials = listOf(
        "Bloques del Nether (300+)",
        "Lava",
        "Tolvas (20+)",
        "Cofres (10)",
        "Gólems de hierro (opcional)",
        "Campanas (opcional)"
    ),
    difficulty = "Media-Difícil",
    production = "Crema de Magma",
    productionRate = "~150 cremas/hora",
    process = "Los magma cubes spawn naturalmente en el Nether. Son atraídos hacia una cámara donde son eliminados. Los drops (crema de magma) caen en tolvas y se almacenan en cofres.",
    tutorialUrl = "https://www.youtube.com/watch?v=ejemplo",
    imageResourceName = "farm_magma_cube",
    tags = listOf("Mob", "Nether", "Automática", "Pociones")
)
```

### Ejemplo 4: Granja Avanzada (Raid)

```kotlin
Farm(
    id = 12,
    name = "Granja de Raid (Trading Hall)",
    description = "Sistema para farmear raids y obtener tótems de inmortalidad además de otros drops.",
    materials = listOf(
        "Aldeanos (20+)",
        "Camas (20+)",
        "Bloques sólidos (500+)",
        "Agua (muchos cubos)",
        "Tolvas (50+)",
        "Cofres (20+)",
        "Campanas",
        "Pociones de debilidad",
        "Manzanas doradas"
    ),
    difficulty = "Muy Difícil",
    production = "Tótems de Inmortalidad, Esmeraldas, Flechas, Armas",
    productionRate = "1-3 tótems por raid",
    process = "Se inicia un raid cerca de una aldea preparada. Los raiders aparecen y son canalizados hacia trampas automáticas. El jugador obtiene el mal augurio y repite el proceso. Requiere mucha preparación y seguridad.",
    tutorialUrl = "https://www.youtube.com/watch?v=ejemplo",
    imageResourceName = "farm_raid",
    tags = listOf("Avanzada", "Boss", "Aldeanos", "Tótem", "Muy Difícil")
)
```

## Tags Comunes

### Por Tipo
- `"Mob"` - Granjas de mobs
- `"Vegetal"` - Granjas de plantas
- `"Recursos"` - Produce recursos básicos
- `"Comida"` - Produce alimentos
- `"Experiencia"` - Para ganar XP

### Por Mecanismo
- `"Automática"` - Funciona sola
- `"Semi-automática"` - Requiere activación manual
- `"AFK"` - Puedes dejarla funcionando
- `"Redstone"` - Usa circuitos de redstone
- `"Aldeanos"` - Requiere aldeanos

### Por Dimensión
- `"Overworld"` - Mundo principal
- `"Nether"` - En el Nether
- `"End"` - En el End

### Por Dificultad
- `"Fácil"` - Fácil de construir
- `"Media"` - Dificultad moderada
- `"Difícil"` - Requiere experiencia
- `"Muy Difícil"` - Muy compleja
- `"Avanzada"` - Para expertos

### Especiales
- `"Esencial"` - Granja importante
- `"Boss"` - Relacionada con jefes
- `"Spawner"` - Usa spawners
- `"Pociones"` - Para brewing
- `"Trading"` - Para comercio con aldeanos

## Dificultades Explicadas

### Fácil
- Pocos materiales
- Diseño simple
- No requiere redstone complejo
- Funciona en cualquier versión
- Ejemplo: Granja de caña, bambú

### Media
- Materiales moderados
- Algo de redstone
- Requiere planificación
- Puede necesitar protección
- Ejemplo: Granja de hierro, creepers

### Difícil
- Muchos materiales
- Redstone avanzado
- Requiere recursos del End/Nether
- Preparación compleja
- Ejemplo: Granja de wither skeleton

### Muy Difícil
- Materiales raros o costosos
- Construcción masiva
- Mecánicas complejas
- Alto riesgo durante construcción
- Ejemplo: Granja de guardián, raid farm

## Consejos para Escribir Descripciones

### Description (Descripción Corta)
- 1-2 líneas máximo
- Resume qué es y para qué sirve
- Menciona el mecanismo principal
- Ejemplo: "Granja automática de X usando Y para obtener Z"

### Process (Cómo Funciona)
- 2-4 oraciones
- Explica el funcionamiento paso a paso
- Menciona el mecanismo de spawn/crecimiento
- Explica cómo se recolectan los items
- Ejemplo: "Los mobs spawn en X, son empujados por Y hacia Z donde mueren. Los drops caen en tolvas."

### Production Rate (Tasa de Producción)
- Especifica cantidad por hora si es posible
- Si es variable, indica "Variable según tamaño"
- Si es muy rápida, usa "Alta producción"
- Ejemplos:
  - "~200 lingotes/hora"
  - "Variable según tamaño"
  - "30+ niveles en minutos"
  - "Alta producción continua"

## Checklist para Agregar una Granja

- [ ] ID único (no repetido)
- [ ] Nombre claro y descriptivo
- [ ] Descripción corta (1-2 líneas)
- [ ] Lista completa de materiales
- [ ] Dificultad apropiada
- [ ] Producción específica
- [ ] Tasa de producción
- [ ] Explicación de funcionamiento
- [ ] URL válida de YouTube
- [ ] Nombre de imagen correcto
- [ ] 3-5 tags relevantes
- [ ] Imagen agregada en drawable/
- [ ] Probado en la app
- [ ] Sin errores de compilación

## Recursos para Encontrar Granjas

### Canales de YouTube Recomendados
- Ilmango
- Rays Works
- Gnembon
- LogicalGeekBoy
- Shulkercraft

### Wikis
- Minecraft Wiki (official)
- Minecraft Technical Wiki
- Reddit: r/technicalminecraft

### Herramientas
- Chunkbase (para encontrar chunks de slime)
- MineAtlas (para encontrar spawners)

## Formato de URLs de YouTube

Siempre usa el formato completo:
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=XXXXXXXXXXX"
```

No uses:
- ❌ `youtu.be/XXXXX`
- ❌ `youtube.com/shorts/XXXXX`
- ❌ `youtube.com/embed/XXXXX`

## Nombrar Imágenes

Sigue este patrón:
```
farm_[nombre_descriptivo].png
```

Reglas:
- Todo en minúsculas
- Usa guiones bajos `_` (no guiones `-`)
- Sin espacios ni caracteres especiales
- Sin extensión en `imageResourceName`

Ejemplos:
- ✅ `farm_bamboo.png` → `"farm_bamboo"`
- ✅ `farm_wither_skeleton.png` → `"farm_wither_skeleton"`
- ❌ `Farm-Bamboo.png`
- ❌ `farm bamboo.png`
- ❌ `farmBamboo.png`

## Versiones de Minecraft

Si una granja solo funciona en versiones específicas, menciónalo en el `process`:

```kotlin
process = "⚠️ Solo funciona en Java Edition 1.19+. Los aldeanos detectan..."
```

o

```kotlin
process = "✅ Compatible con Java y Bedrock. El bambú crece hasta..."
```

---

¡Con estos ejemplos ya puedes agregar fácilmente más granjas a tu wiki! 🎮⛏️
