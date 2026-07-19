# Despliegue de VM sin código fuente

La VM no compila ni necesita el proyecto. Este directorio contiene solamente la configuración que conecta las imágenes publicadas en DockerLab/Docker Hub.

## Archivos necesarios en la VM

- `.env`
- `docker-compose.yml`

El inicializador de Kafka está incluido dentro del propio Compose y Oracle XE crea el esquema mediante JPA (`ddl-auto=update`), por lo que no se montan archivos del código fuente.

## Uso

```bash
mkdir -p ~/medialert-deploy
cd ~/medialert-deploy
cp .env.example .env
nano .env
docker login
docker compose --env-file .env pull
docker compose --env-file .env up -d
docker compose --env-file .env ps -a
curl -i http://localhost:8080/api/bff/health
```

Antes de levantar, publique localmente las imágenes con tag `3.0.1`.
