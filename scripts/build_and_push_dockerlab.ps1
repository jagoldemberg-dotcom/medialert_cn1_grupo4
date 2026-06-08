param(
  [string]$DockerlabUser = "j4cobiyo",
  [string]$Version = "1.0.0"
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

Write-Host "Construyendo microservicio..."
docker build -t "$DockerlabUser/microservicio-alertas:$Version" ./microservicio-alertas

Write-Host "Construyendo BFF..."
docker build -t "$DockerlabUser/bff-alertas:$Version" ./bff-alertas

Write-Host "Publicando imagenes en DockerLab/Docker registry..."
docker push "$DockerlabUser/microservicio-alertas:$Version"
docker push "$DockerlabUser/bff-alertas:$Version"

Write-Host "Imagenes publicadas:"
Write-Host "- $DockerlabUser/microservicio-alertas:$Version"
Write-Host "- $DockerlabUser/bff-alertas:$Version"
Write-Host "Oracle se usa en la VM con la imagen publica gvenzl/oracle-xe:21-slim."
