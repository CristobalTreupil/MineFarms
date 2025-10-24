# Script para verificar las imágenes de MineFarms
# Ejecuta este script en PowerShell para verificar si todas las imágenes están en su lugar

$drawablePath = "C:\Users\Cris\Documents\Android\MineFarms\app\src\main\res\drawable"

Write-Host "`n==================================================" -ForegroundColor Cyan
Write-Host "  VERIFICADOR DE IMÁGENES - MineFarms Wiki" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

Write-Host "`nUbicación de imágenes:" -ForegroundColor Yellow
Write-Host $drawablePath

# Verifica si la carpeta existe
if (-not (Test-Path $drawablePath)) {
    Write-Host "`n❌ ERROR: La carpeta drawable no existe." -ForegroundColor Red
    Write-Host "Creando carpeta..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $drawablePath -Force | Out-Null
    Write-Host "✅ Carpeta creada exitosamente.`n" -ForegroundColor Green
}

# Lista de imágenes requeridas
$requiredImages = @(
    "farm_iron_golem",
    "farm_enderman",
    "farm_creeper",
    "farm_sugar_cane",
    "farm_slime",
    "farm_crops",
    "farm_wither_skeleton",
    "farm_guardian"
)

$foundCount = 0
$missingCount = 0

Write-Host "`n📋 Verificando imágenes requeridas...`n" -ForegroundColor Yellow

foreach ($imageName in $requiredImages) {
    $found = $false
    $extensions = @("*.png", "*.jpg", "*.jpeg", "*.webp")
    
    foreach ($ext in $extensions) {
        $pattern = Join-Path $drawablePath "$imageName$($ext.Substring(1))"
        if (Test-Path $pattern) {
            $file = Get-Item $pattern
            Write-Host "  ✅ $imageName encontrado: $($file.Name)" -ForegroundColor Green
            Write-Host "     Tamaño: $([math]::Round($file.Length / 1KB, 2)) KB" -ForegroundColor Gray
            $found = $true
            $foundCount++
            break
        }
    }
    
    if (-not $found) {
        Write-Host "  ❌ $imageName - FALTA" -ForegroundColor Red
        Write-Host "     Necesitas: $imageName.png (o .jpg, .webp)" -ForegroundColor Gray
        $missingCount++
    }
}

Write-Host "`n==================================================" -ForegroundColor Cyan
Write-Host "  RESUMEN" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "`n  Total requerido: 8 imágenes" -ForegroundColor White
Write-Host "  ✅ Encontradas: $foundCount" -ForegroundColor Green
Write-Host "  ❌ Faltantes: $missingCount" -ForegroundColor $(if ($missingCount -eq 0) { "Green" } else { "Red" })

if ($missingCount -eq 0) {
    Write-Host "`n🎉 ¡PERFECTO! Todas las imágenes están en su lugar." -ForegroundColor Green
    Write-Host "   Ahora sincroniza Gradle en Android Studio y ejecuta la app.`n" -ForegroundColor Yellow
} else {
    Write-Host "`n⚠️  Faltan $missingCount imágenes." -ForegroundColor Yellow
    Write-Host "   Lee COMO_AGREGAR_IMAGENES.md para más información.`n" -ForegroundColor Gray
}

# Lista todos los archivos en drawable
Write-Host "`n📁 Archivos actuales en drawable:" -ForegroundColor Yellow
$files = Get-ChildItem -Path $drawablePath -File
if ($files.Count -eq 0) {
    Write-Host "   (vacío)`n" -ForegroundColor Gray
} else {
    foreach ($file in $files) {
        Write-Host "   - $($file.Name)" -ForegroundColor Gray
    }
    Write-Host ""
}

Write-Host "Presiona cualquier tecla para salir..." -ForegroundColor Cyan
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
