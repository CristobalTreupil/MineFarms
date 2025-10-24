# Guía para Agregar Imágenes a las Granjas

## Ubicación de las Imágenes

Todas las imágenes deben colocarse en:
```
app/src/main/res/drawable/
```

## Nombres de Archivo Requeridos

Las imágenes deben tener exactamente estos nombres (puedes usar .png, .jpg o .webp):

1. `farm_iron_golem.png` - Granja de Hierro con Aldeanos
2. `farm_enderman.png` - Granja de Experiencia con Enderman
3. `farm_creeper.png` - Granja de Creepers para Pólvora
4. `farm_sugar_cane.png` - Granja de Caña de Azúcar
5. `farm_slime.png` - Granja de Slimes
6. `farm_crops.png` - Granja de Cultivos Automática
7. `farm_wither_skeleton.png` - Granja de Wither Esqueletos
8. `farm_guardian.png` - Granja de Guardian

## Cómo Obtener las Imágenes

### Opción 1: Capturas de Pantalla de Minecraft
1. Construye la granja en Minecraft
2. Toma una captura de pantalla (F2 en Java Edition)
3. Edita la imagen para que sea cuadrada o rectangular
4. Redimensiona a 800x600 px o similar
5. Guarda con el nombre correcto

### Opción 2: Capturas de Videos de YouTube
1. Encuentra el tutorial de YouTube de la granja
2. Pausa el video en un buen momento que muestre la granja
3. Toma una captura de pantalla
4. Recorta y edita la imagen
5. Guarda con el nombre correcto

### Opción 3: Usar Imágenes de Internet
1. Busca imágenes de la granja en Google
2. Asegúrate de tener derecho a usar la imagen
3. Descarga y edita si es necesario
4. Guarda con el nombre correcto

### Opción 4: Crear Renders con Software 3D
1. Usa programas como Blender con extensiones de Minecraft
2. Crea un render de la granja
3. Exporta como PNG
4. Guarda con el nombre correcto

## Especificaciones Técnicas

- **Formato**: PNG (recomendado), JPG o WebP
- **Resolución recomendada**: 800x600 px o 1024x768 px
- **Proporción**: Horizontal (4:3 o 16:9)
- **Tamaño de archivo**: Menos de 500 KB por imagen
- **Nombres**: Minúsculas, sin espacios, usar guiones bajos

## Ejemplo de Estructura de Carpetas

```
app/src/main/res/drawable/
├── farm_iron_golem.png
├── farm_enderman.png
├── farm_creeper.png
├── farm_sugar_cane.png
├── farm_slime.png
├── farm_crops.png
├── farm_wither_skeleton.png
└── farm_guardian.png
```

## Verificación

Después de agregar las imágenes:

1. Sincroniza el proyecto en Android Studio (Sync Project with Gradle Files)
2. Compila la aplicación
3. Las imágenes deberían aparecer automáticamente en la lista y detalles

## Troubleshooting

### Las imágenes no aparecen:
- Verifica que los nombres sean exactamente como se especifica
- Asegúrate de que las imágenes estén en `drawable/` y no en `drawable-v24/` u otras carpetas
- Sincroniza Gradle
- Limpia y reconstruye el proyecto (Build > Clean Project, luego Build > Rebuild Project)

### Errores de compilación:
- Verifica que los nombres de archivo no tengan caracteres especiales
- Usa solo letras minúsculas, números y guiones bajos
- No uses guiones (-), usa guiones bajos (_)
- No uses espacios ni caracteres especiales

## Personalización

Si quieres agregar más granjas, actualiza:
1. `FarmRepository.kt` - Agrega la nueva granja con su `imageResourceName`
2. Agrega la imagen correspondiente en `drawable/`
3. La imagen se cargará automáticamente

## Imágenes Temporales

Si no tienes imágenes disponibles, la app mostrará un emoji placeholder (🖼️ en lista, 🏗️ en detalles).
