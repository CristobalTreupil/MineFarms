# MineFarms Wiki - Granjas Técnicas de Minecraft

Una aplicación Android Wiki para consultar información sobre granjas técnicas de Minecraft, incluyendo materiales necesarios, dificultad, producción y tutoriales en video.

## Características

- ✅ Lista de granjas técnicas populares
- ✅ Información detallada de cada granja
- ✅ Materiales necesarios para construir
- ✅ Nivel de dificultad
- ✅ Tasa de producción
- ✅ Enlaces directos a tutoriales en YouTube
- ✅ Categorización por etiquetas
- ✅ Interfaz moderna con Material Design 3

## Granjas Incluidas

1. **Granja de Hierro con Aldeanos** - Producción automática de hierro
2. **Granja de Experiencia con Enderman** - XP rápida en el End
3. **Granja de Creepers para Pólvora** - Pólvora automática
4. **Granja de Caña de Azúcar** - Caña automática con redstone
5. **Granja de Slimes** - Bolas de slime en chunk específico
6. **Granja de Cultivos Automática** - Agricultura con aldeanos
7. **Granja de Wither Esqueletos** - Cráneos para el Wither
8. **Granja de Guardian** - Prismarinos y XP del océano

## Cómo Agregar Imágenes

Para que las imágenes de las granjas se muestren en la aplicación:

1. **Crea las imágenes** de cada granja (capturas de pantalla o renders)
   - Formato recomendado: PNG o JPG
   - Resolución recomendada: 800x600 px o similar
   - Nombres según el campo `imageResourceName` en cada Farm

2. **Agrega las imágenes a la carpeta de recursos:**
   ```
   app/src/main/res/drawable/
   ```

3. **Nombres de archivo requeridos:**
   - `farm_iron_golem.png` - Granja de Hierro
   - `farm_enderman.png` - Granja de Enderman
   - `farm_creeper.png` - Granja de Creepers
   - `farm_sugar_cane.png` - Granja de Caña de Azúcar
   - `farm_slime.png` - Granja de Slimes
   - `farm_crops.png` - Granja de Cultivos
   - `farm_wither_skeleton.png` - Granja de Wither Esqueletos
   - `farm_guardian.png` - Granja de Guardian

4. **Actualiza el código para mostrar las imágenes:**
   
   En `FarmListScreen.kt` y `FarmDetailScreen.kt`, descomenta las secciones de Image y agrega:
   
   ```kotlin
   val imageId = context.resources.getIdentifier(
       farm.imageResourceName,
       "drawable",
       context.packageName
   )
   
   if (imageId != 0) {
       Image(
           painter = painterResource(id = imageId),
           contentDescription = farm.name,
           modifier = Modifier.fillMaxSize(),
           contentScale = ContentScale.Crop
       )
   }
   ```

## Estructura del Proyecto

```
app/src/main/java/com/example/minefarms/
├── model/
│   └── Farm.kt                    # Modelo de datos de la granja
├── repository/
│   └── FarmRepository.kt          # Repositorio con datos de granjas
├── viewmodel/
│   └── FarmViewModel.kt           # ViewModel para la lista
├── ui/
│   ├── screens/
│   │   ├── FarmListScreen.kt      # Pantalla de lista
│   │   └── FarmDetailScreen.kt    # Pantalla de detalles
│   ├── navigation/
│   │   └── Navigation.kt          # Navegación de la app
│   └── theme/
│       └── ...                    # Tema de Material Design
└── MainActivity.kt                # Actividad principal
```

## Cómo Agregar Más Granjas

Para agregar una nueva granja, edita `FarmRepository.kt`:

```kotlin
Farm(
    id = 9, // Siguiente ID disponible
    name = "Nombre de la Granja",
    description = "Descripción breve",
    materials = listOf(
        "Material 1",
        "Material 2",
        // ...
    ),
    difficulty = "Fácil/Media/Difícil",
    production = "Lo que produce",
    productionRate = "Tasa de producción",
    process = "Explicación de cómo funciona",
    tutorialUrl = "https://youtube.com/watch?v=...",
    imageResourceName = "nombre_imagen_sin_extension",
    tags = listOf("Tag1", "Tag2", "Tag3")
)
```

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Diseño visual
- **Navigation Component** - Navegación entre pantallas
- **ViewModel** - Arquitectura MVVM

## Requisitos

- Android Studio Hedgehog o superior
- SDK mínimo: 24 (Android 7.0)
- SDK objetivo: 36

## Instalación

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza Gradle
4. Agrega las imágenes en `res/drawable/`
5. Ejecuta la aplicación

## Próximas Mejoras

- [ ] Agregar imágenes reales de las granjas
- [ ] Función de búsqueda y filtrado
- [ ] Favoritos
- [ ] Modo oscuro
- [ ] Compartir granjas
- [ ] Más granjas técnicas

## Créditos

Tutoriales de granjas de diversos creadores de contenido de Minecraft.

## Licencia

MIT License - Libre para uso educativo y personal
