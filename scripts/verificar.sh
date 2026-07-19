#!/usr/bin/env bash
set -Eeuo pipefail
docker compose ps -a
curl -fsS http://localhost:8081/actuator/health; echo
curl -fsS http://localhost:8082/actuator/health; echo
curl -fsS http://localhost:8083/actuator/health; echo
curl -fsS http://localhost:8084/actuator/health; echo
curl -fsS http://localhost:8080/api/bff/health; echo
docker compose exec -T kafka1 kafka-topics --bootstrap-server kafka1:29092 --list
