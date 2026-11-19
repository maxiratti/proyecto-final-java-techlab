$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
$mavenTechlab = 'D:\Herramientas\apache-maven-3.10.1\bin\mvn.cmd'
if (-not (Test-Path $mavenTechlab)) {
    $comandoMaven = Get-Command mvn -ErrorAction SilentlyContinue
    if ($comandoMaven) { $mavenTechlab = $comandoMaven.Source }
    else { throw 'No se encontró Maven. Editá la ruta al inicio de INICIAR.ps1.' }
}
# JAVA_HOME para esta terminal, sin modificar la configuración permanente de Windows.
if (-not $env:JAVA_HOME) {
    $javaTechlab = Get-Command javac -ErrorAction Stop
    $env:JAVA_HOME = Split-Path (Split-Path $javaTechlab.Source -Parent) -Parent
}
$claveTechlab = Read-Host 'Contraseña de MySQL root (no se muestra)' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $claveTechlab).Password
try {
    Write-Host 'Iniciando TECHLAB. Cuando aparezca Started ArticuloApiApplication, abrí http://localhost:8080'
    & $mavenTechlab spring-boot:run
} finally {
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
}
