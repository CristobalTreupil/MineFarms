# 🖼️ Resumen de Imágenes Implementadas

## ✅ Estado Actual

Todas las **8 imágenes** han sido correctamente vinculadas a sus respectivas granjas.

## 📋 Listado Completo

| ID | Nombre de Granja | Archivo de Imagen | Estado |
|----|-----------------|-------------------|---------|
| 1 | Granja de Hierro con Aldeanos | `granja_de_hierro.jpg` | ✅ Implementado |
| 2 | Granja de Experiencia con Enderman | `granja_de_experiencia_con_enderman.jpg` | ✅ Implementado |
| 3 | Granja de Creepers para Pólvora | `granja_de_creepers_para_polvora.jpg` | ✅ Implementado |
| 4 | Granja de Caña de Azúcar | `granja_de_cana_de_azucar.jpg` | ✅ Implementado |
| 5 | Granja de Slimes | `granja_de_slime.jpg` | ✅ Implementado |
| 6 | Granja de Cultivos Automática | `granja_de_cultivos_automatica.jpg` | ✅ Implementado |
| 7 | Granja de Wither Esqueletos | `granja_de_wither_esqueletos.jpg` | ✅ Implementado |
| 8 | Granja de Guardian para Prismarinos | `granja_de_guardianes.jpg` | ✅ Implementado |

## 📍 Ubicación de Imágenes

```
app/src/main/res/drawable/
```

## 🔧 Cómo se Implementaron

Las imágenes se vincularon mediante el campo `imageResourceName` en cada objeto `Farm` dentro de:

```
app/src/main/java/com/example/minefarms/repository/FarmRepository.kt
```

## 📱 Dónde se Muestran las Imágenes

### 1. **FarmListScreen** (Lista de Granjas)
- Muestra un preview pequeño de cada granja
- Tamaño: 80dp de altura
- Ubicación: Lado izquierdo de cada tarjeta

### 2. **FarmDetailScreen** (Detalle de Granja)
- Muestra la imagen completa en grande
- Tamaño: 220dp de altura, ancho completo
- Ubicación: Top de la pantalla, debajo del título

### 3. **ProfileScreen** (Pestañas de Favoritos y Guardadas)
- Muestra las granjas que has marcado con like o guardado
- Usa el mismo formato que FarmListScreen

## 🎨 Formato de las Imágenes

- **Formato:** JPG
- **Nombrado:** snake_case (todo minúsculas, separado por guiones bajos)
- **Compatibilidad:** ✅ Android compatible (sin espacios ni caracteres especiales)

## 🔄 Cómo Reemplazar una Imagen

1. Ve a `app/src/main/res/drawable/`
2. Borra la imagen actual (ejemplo: `granja_de_hierro.jpg`)
3. Copia tu nueva imagen con el **mismo nombre**
4. Asegúrate de que sea JPG o PNG
5. El nombre debe estar todo en minúsculas y usar guiones bajos (_)

## ➕ Cómo Agregar Nuevas Imágenes

1. Coloca la imagen en `app/src/main/res/drawable/`
2. Usa un nombre descriptivo en snake_case (ejemplo: `granja_de_oro.jpg`)
3. Abre `FarmRepository.kt`
4. Crea una nueva granja con:
   ```kotlin
   imageResourceName = "granja_de_oro"  // Sin la extensión .jpg
   ```

## ⚠️ Reglas Importantes

- ❌ NO uses espacios en los nombres
- ❌ NO uses mayúsculas
- ❌ NO uses caracteres especiales (ñ, á, é, etc.)
- ✅ Usa solo letras minúsculas, números y guiones bajos (_)
- ✅ NO incluyas la extensión (.jpg) en `imageResourceName`

## 🐛 Solución de Problemas

### Problema: La imagen no aparece
**Solución:**
1. Verifica que el nombre en `imageResourceName` coincida exactamente con el archivo
2. Asegúrate de que el archivo esté en la carpeta `drawable`
3. Limpia y recompila: `.\gradlew.bat clean assembleDebug`

### Problema: Error de compilación
**Solución:**
1. Verifica que el nombre del archivo no tenga espacios
2. Asegúrate de que esté en minúsculas
3. Renombra el archivo si es necesario

---

**✨ Todo está listo y funcionando!** Las 8 granjas tienen sus imágenes correctamente vinculadas.
