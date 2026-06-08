#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

echo "Levantando stack MediAlert en la VM..."
docker compose --env-file .env.vm -f docker-compose.vm.yml pull microservicio-alertas bff-alertas || true
docker compose --env-file .env.vm -f docker-compose.vm.yml up -d oracle-alertas

echo "Esperando 90 segundos para que Oracle inicie..."
sleep 90

docker compose --env-file .env.vm -f docker-compose.vm.yml up -d microservicio-alertas bff-alertas

echo "Contenedores:"
docker ps

echo "Prueba BFF local:"
curl -H "X-Api-Gateway-Secret: mi-secreto-api-gateway-123" http://localhost:8080/api/bff/health || true
