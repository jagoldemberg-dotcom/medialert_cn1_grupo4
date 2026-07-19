#!/usr/bin/env bash
set -Eeuo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${1:-$SCRIPT_DIR/.env.vm.oracle-cloud}"
COMPOSE_FILE="$SCRIPT_DIR/docker-compose.vm.oracle-cloud.yml"

[[ -f "$ENV_FILE" ]] || {
  echo "Falta $ENV_FILE. Copia .env.vm.oracle-cloud.example y completalo." >&2
  exit 1
}

if docker ps --format '{{.Names}}' | grep -qx 'bff-alertas'; then
  echo "Se encontro el contenedor antiguo bff-alertas; se reemplazara por el BFF del Compose final."
  docker rm -f bff-alertas
fi

if command -v ss >/dev/null 2>&1 && ss -ltn | awk '{print $4}' | grep -Eq '(^|:)8080$'; then
  echo "El puerto 8080 sigue ocupado. Revisa: docker ps --format 'table {{.Names}}\t{{.Ports}}' | grep 8080" >&2
  exit 1
fi

docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" pull
docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" up -d
docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" ps -a
