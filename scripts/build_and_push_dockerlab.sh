#!/usr/bin/env bash
set -Eeuo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
USER_NAME="${DOCKERLAB_USER:-j4cobiyo}"
TAG="${IMAGE_TAG:-3.0.1}"
for service in bff-alertas servicio-alertas streaming-alertas productor-senales procesador-senales; do
  image="$USER_NAME/$service:$TAG"
  echo "== Construyendo $image =="
  docker build -t "$image" "$ROOT/$service"
  docker push "$image"
done
echo "Imágenes publicadas correctamente."
