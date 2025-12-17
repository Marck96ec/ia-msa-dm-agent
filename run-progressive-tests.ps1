# ============================================================================
# Script de Prueba: Memoria Progresiva Sin Interrogatorio
# ============================================================================
# Ejecutar después de que la aplicación esté completamente iniciada
# Uso: .\run-progressive-tests.ps1
# ============================================================================

Write-Host "`n🧪 INICIANDO TESTS DE MEMORIA PROGRESIVA`n" -ForegroundColor Cyan

$baseUrl = "http://localhost:8080/chat"
$testsPassed = 0
$testsFailed = 0

# ============================================================================
# TEST 1: Detección de Formato STEPS
# ============================================================================
Write-Host "📝 Test 1: Detección de Formato STEPS (comando 'en pasos')" -ForegroundColor Yellow

$body1 = @{
    message = "Explícame en pasos cómo organizar un baby shower exitoso"
    metadata = @{
        userId = "test-prog-001"
        mode = "EVENT"
    }
} | ConvertTo-Json -Compress

try {
    $response1 = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $body1 -TimeoutSec 30
    Write-Host "✅ Test 1 PASS: Respuesta recibida" -ForegroundColor Green
    Write-Host "   UserID: $($response1.userId)" -ForegroundColor Gray
    Write-Host "   Primeros 200 chars:" -ForegroundColor Gray
    Write-Host "   $($response1.aiResponse.Substring(0, [Math]::Min(200, $response1.aiResponse.Length)))..." -ForegroundColor White
    $testsPassed++
} catch {
    Write-Host "❌ Test 1 FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testsFailed++
}

Start-Sleep -Seconds 2

# ============================================================================
# TEST 2: Detección de Ritmo QUICK
# ============================================================================
Write-Host "`n📝 Test 2: Detección de Ritmo QUICK (comando 'rápido')" -ForegroundColor Yellow

$body2 = @{
    message = "Rápido, dime 3 juegos para baby shower"
    metadata = @{
        userId = "test-prog-002"
        mode = "EVENT"
    }
} | ConvertTo-Json -Compress

try {
    $response2 = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $body2 -TimeoutSec 30
    Write-Host "✅ Test 2 PASS: Respuesta recibida" -ForegroundColor Green
    Write-Host "   UserID: $($response2.userId)" -ForegroundColor Gray
    Write-Host "   Primeros 200 chars:" -ForegroundColor Gray
    Write-Host "   $($response2.aiResponse.Substring(0, [Math]::Min(200, $response2.aiResponse.Length)))..." -ForegroundColor White
    $testsPassed++
} catch {
    Write-Host "❌ Test 2 FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testsFailed++
}

Start-Sleep -Seconds 2

# ============================================================================
# TEST 3: Detección de Formato LIST
# ============================================================================
Write-Host "`n📝 Test 3: Detección de Formato LIST (comando 'en lista')" -ForegroundColor Yellow

$body3 = @{
    message = "Dame ideas de decoración en lista"
    metadata = @{
        userId = "test-prog-003"
        mode = "EVENT"
    }
} | ConvertTo-Json -Compress

try {
    $response3 = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $body3 -TimeoutSec 30
    Write-Host "✅ Test 3 PASS: Respuesta recibida" -ForegroundColor Green
    Write-Host "   UserID: $($response3.userId)" -ForegroundColor Gray
    Write-Host "   Primeros 200 chars:" -ForegroundColor Gray
    Write-Host "   $($response3.aiResponse.Substring(0, [Math]::Min(200, $response3.aiResponse.Length)))..." -ForegroundColor White
    $testsPassed++
} catch {
    Write-Host "❌ Test 3 FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testsFailed++
}

Start-Sleep -Seconds 2

# ============================================================================
# TEST 4: Detección de Formato DIRECT
# ============================================================================
Write-Host "`n📝 Test 4: Detección de Formato DIRECT (comando 'al grano')" -ForegroundColor Yellow

$body4 = @{
    message = "Vamos al grano, ¿cuánto cuesta un baby shower promedio?"
    metadata = @{
        userId = "test-prog-004"
        mode = "EVENT"
    }
} | ConvertTo-Json -Compress

try {
    $response4 = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $body4 -TimeoutSec 30
    Write-Host "✅ Test 4 PASS: Respuesta recibida" -ForegroundColor Green
    Write-Host "   UserID: $($response4.userId)" -ForegroundColor Gray
    Write-Host "   Primeros 200 chars:" -ForegroundColor Gray
    Write-Host "   $($response4.aiResponse.Substring(0, [Math]::Min(200, $response4.aiResponse.Length)))..." -ForegroundColor White
    $testsPassed++
} catch {
    Write-Host "❌ Test 4 FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testsFailed++
}

Start-Sleep -Seconds 2

# ============================================================================
# TEST 5: Detección de Ritmo EXPLAINED
# ============================================================================
Write-Host "`n📝 Test 5: Detección de Ritmo EXPLAINED (comando 'explícame')" -ForegroundColor Yellow

$body5 = @{
    message = "Explícame con ejemplos cómo elegir el tema del baby shower"
    metadata = @{
        userId = "test-prog-005"
        mode = "EVENT"
    }
} | ConvertTo-Json -Compress

try {
    $response5 = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $body5 -TimeoutSec 30
    Write-Host "✅ Test 5 PASS: Respuesta recibida" -ForegroundColor Green
    Write-Host "   UserID: $($response5.userId)" -ForegroundColor Gray
    Write-Host "   Primeros 200 chars:" -ForegroundColor Gray
    Write-Host "   $($response5.aiResponse.Substring(0, [Math]::Min(200, $response5.aiResponse.Length)))..." -ForegroundColor White
    $testsPassed++
} catch {
    Write-Host "❌ Test 5 FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testsFailed++
}

Start-Sleep -Seconds 2

# ============================================================================
# TEST 6: Combinación de Preferencias
# ============================================================================
Write-Host "`n📝 Test 6: Combinación (Formal + Detallado + Pasos)" -ForegroundColor Yellow

$body6 = @{
    message = "Quiero que me respondas de forma más formal y detallada, en pasos"
    metadata = @{
        userId = "test-prog-006"
    }
} | ConvertTo-Json -Compress

try {
    $response6 = Invoke-RestMethod -Uri $baseUrl -Method Post -ContentType "application/json" -Body $body6 -TimeoutSec 30
    Write-Host "✅ Test 6 PASS: Respuesta recibida" -ForegroundColor Green
    Write-Host "   UserID: $($response6.userId)" -ForegroundColor Gray
    Write-Host "   Primeros 200 chars:" -ForegroundColor Gray
    Write-Host "   $($response6.aiResponse.Substring(0, [Math]::Min(200, $response6.aiResponse.Length)))..." -ForegroundColor White
    $testsPassed++
} catch {
    Write-Host "❌ Test 6 FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testsFailed++
}

# ============================================================================
# RESUMEN
# ============================================================================
Write-Host "`n" -NoNewline
Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "           RESUMEN DE TESTS" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Tests Pasados: $testsPassed" -ForegroundColor Green
Write-Host "Tests Fallidos: $testsFailed" -ForegroundColor $(if ($testsFailed -eq 0) { "Green" } else { "Red" })
Write-Host "════════════════════════════════════════`n" -ForegroundColor Cyan

# ============================================================================
# VERIFICACIÓN EN BASE DE DATOS
# ============================================================================
Write-Host ""
Write-Host "Para verificar en BD, ejecuta:" -ForegroundColor Yellow
Write-Host 'psql -U myuser -d mydb -c "SELECT user_id, preferred_format, response_speed, tone, verbosity FROM user_profile WHERE user_id LIKE ''test-prog-%'' ORDER BY user_id;"' -ForegroundColor White

Write-Host ""
Write-Host "Valores esperados:" -ForegroundColor Yellow
Write-Host "  test-prog-001: preferred_format = STEPS" -ForegroundColor Gray
Write-Host "  test-prog-002: response_speed = QUICK" -ForegroundColor Gray
Write-Host "  test-prog-003: preferred_format = LIST" -ForegroundColor Gray
Write-Host "  test-prog-004: preferred_format = DIRECT" -ForegroundColor Gray
Write-Host "  test-prog-005: response_speed = EXPLAINED" -ForegroundColor Gray
Write-Host "  test-prog-006: tone = FORMAL, verbosity = DETAILED, preferred_format = STEPS" -ForegroundColor Gray
Write-Host ""
