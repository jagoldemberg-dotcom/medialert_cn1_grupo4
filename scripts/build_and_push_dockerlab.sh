#!/usr/bin/env bash
set -euo pipefail

DOCKERLAB_USER=${1:-j4cobiyo}
VERSION=${2:-1.0.0}

cd "$(dirname "$0")/.."

echo "Construyendo microservicio..."
docker build -t "$DOCKERLAB_USER/microservicio-alertas:$VERSION" ./microservicio-alertas

echo "Construyendo BFF..."
docker build -t "$DOCKERLAB_USER/bff-alertas:$VERSION" ./bff-alertas

echo "Publicando imagenes en DockerLab/Docker registry..."
docker push "$DOCKERLAB_USER/microservicio-alertas:$VERSION"
docker push "$DOCKERLAB_USER/bff-alertas:$VERSION"

echo "Imagenes publicadas:"
echo "- $DOCKERLAB_USER/microservicio-alertas:$VERSION"
echo "- $DOCKERLAB_USER/bff-alertas:$VERSION"
echo "Oracle se usa en la VM con la imagen publica gvenzl/oracle-xe:21-slim."
