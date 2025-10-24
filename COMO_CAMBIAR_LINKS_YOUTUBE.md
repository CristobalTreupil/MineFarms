# 📹 Cómo Cambiar los Links de YouTube

## 📍 Ubicación del Archivo

Los links de YouTube de los tutoriales están en:

```
app/src/main/java/com/example/minefarms/repository/FarmRepository.kt
```

## 🎯 Cómo Editar

Cada granja tiene un campo `tutorialUrl` que contiene el link de YouTube. Aquí están los links actuales:

### **Granja 1: Hierro con Aldeanos**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=VP8CQtDz1Jg",
```

### **Granja 2: Experiencia con Enderman**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=3S5IR3n4yGo",
```

### **Granja 3: Creepers para Pólvora**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=qXy8e5yxFMQ",
```

### **Granja 4: Caña de Azúcar**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=SQVI2z6BwpQ",
```

### **Granja 5: Slimes**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=iKTroAfLzQ0",
```

### **Granja 6: Cultivos Automática**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=cC48dAYXjA8",
```

### **Granja 7: Wither Esqueletos**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=N66r0BbCxCo",
```

### **Granja 8: Guardian para Prismarinos**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=CTt_3s5OqGQ",
```

## ✏️ Instrucciones para Cambiar

1. Abre el archivo `FarmRepository.kt` en Android Studio
2. Busca la granja que quieres modificar (por ejemplo: `id = 1` para la granja de hierro)
3. Encuentra la línea que dice `tutorialUrl = "..."`
4. Reemplaza el link completo entre las comillas
5. Guarda el archivo (Ctrl + S)
6. Compila el proyecto: `.\gradlew.bat assembleDebug`

## 📝 Ejemplo de Cambio

**Antes:**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=VP8CQtDz1Jg",
```

**Después:**
```kotlin
tutorialUrl = "https://www.youtube.com/watch?v=TU_NUEVO_VIDEO_ID",
```

## ⚠️ Importante

- Mantén las comillas dobles `""`
- Mantén la coma `,` al final
- Usa el formato completo de YouTube: `https://www.youtube.com/watch?v=VIDEO_ID`
- También funciona con links cortos: `https://youtu.be/VIDEO_ID`

## 🖼️ Cambiar Imágenes

Las imágenes están vinculadas automáticamente en el mismo archivo mediante el campo `imageResourceName`.

**Archivos de imagen actuales:**
- `granja_de_hierro.jpg` → Granja 1
- `granja_de_experiencia_con_enderman.jpg` → Granja 2
- `granja_de_creepers_para_polvora.jpg` → Granja 3
- `granja_de_cana_de_azucar.jpg` → Granja 4
- `granja_de_slime.jpg` → Granja 5
- `granja_de_cultivos_automatica.jpg` → Granja 6
- `granja_de_wither_esqueletos.jpg` → Granja 7
- `granja_de_guardianes.jpg` → Granja 8

Para cambiar una imagen:
1. Reemplaza el archivo JPG en `app/src/main/res/drawable/`
2. Usa el mismo nombre de archivo
3. O cambia el `imageResourceName` en `FarmRepository.kt`

---

**Tip:** Si quieres agregar más granjas, copia el bloque completo de una granja existente, cambia el `id`, y modifica todos los campos.
