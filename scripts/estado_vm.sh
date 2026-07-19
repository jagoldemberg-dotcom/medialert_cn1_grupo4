#!/usr/bin/env bash
set -Eeuo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${1:-$SCRIPT_DIR/.env.vm}"
COMPOSE_FILE="${2:-$SCRIPT_DIR/docker-compose.vm.yml}"

[[ -f "$ENV_FILE" ]] || { echo "No existe $ENV_FILE" >&2; exit 1; }

echo "=== Estado de contenedores ==="
docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" ps -a

echo
echo "=== Codigo kafka-init ==="
container_id="$(docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" ps -q kafka-init || true)"
if [[ -n "$container_id" ]]; then
  docker inspect "$container_id" --format='ExitCode={{.State.ExitCode}} Estado={{.State.Status}}'
fi

echo
echo "=== Topicos Kafka ==="
kafka_id="$(docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" ps -q kafka1 || true)"
if [[ -n "$kafka_id" ]]; then
  docker exec "$kafka_id" kafka-topics --bootstrap-server kafka1:29092 --list || true
fi

echo
echo "=== Health BFF ==="
curl -fsS http://localhost:8080/api/bff/health || true
echo
