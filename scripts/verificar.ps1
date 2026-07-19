Write-Host "== Estado de contenedores =="
docker compose ps -a
Invoke-RestMethod http://localhost:8081/actuator/health
Invoke-RestMethod http://localhost:8082/actuator/health
Invoke-RestMethod http://localhost:8083/actuator/health
Invoke-RestMethod http://localhost:8084/actuator/health
Invoke-RestMethod http://localhost:8080/api/bff/health
