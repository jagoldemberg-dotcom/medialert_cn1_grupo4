# MediAlert EFT - Grupo 4

Sistema de alertas médicas en tiempo real desarrollado sobre el código de las experiencias anteriores. Integra Angular 18, Microsoft Entra ID, Spring Boot, Spring Security, un BFF, RabbitMQ, Kafka, Oracle, AWS API Gateway y Docker.

## Arquitectura final

```text
Angular local :4200 + Microsoft Entra ID
                 |
                 v
AWS API Gateway existente /prod/api/bff + recurso comodín /*
                 |
                 v
BFF Spring Boot :8080 + Spring Security
       |                         |
       v                         v
RabbitMQ                    CRUD/consultas
  - cola.alertas.vitales          |
  - cola.resumen.vitales          v
       |                    servicio-alertas :8083
       +--------------------> Oracle ALERTA_VITAL / RESUMEN_VITAL
                                      |
                                      v
                              Kafka alertas-medicas y resumenes-vitales
                                      |
                                      v
                              streaming-alertas :8084
                                      |
                                      +----> BFF ----> Angular
```

También se conserva el flujo de la experiencia 3: `productor-senales -> senales_vitales -> procesador-senales -> alertas -> servicio-alertas`.

## Estructura

- `frontend-angular18-azuread`: frontend que se ejecuta localmente.
- `bff-alertas`: API pública, Spring Security, validación y publicación RabbitMQ.
- `servicio-alertas`: consumidores de colas, CRUD, Oracle, idempotencia y productor Kafka.
- `streaming-alertas`: consumidor Kafka, eventos recientes y estadísticas.
- `productor-senales` y `procesador-senales`: flujo de streaming de la experiencia 3.
- `scripts/docker-compose.vm.yml`: VM AWS usando imágenes DockerLab.
- `deploy-vm`: configuración autónoma para la VM, sin código fuente ni montajes locales.
- `documentacion`: manual, formato de respuesta, guion y evidencias.

## Ejecución backend local

```bash
cp .env.example .env
docker compose --env-file .env up -d --build
docker compose --env-file .env ps
```

Servicios:

| Servicio | URL |
|---|---|
| BFF | http://localhost:8080/api/bff/health |
| Productor | http://localhost:8081/actuator/health |
| Procesador | http://localhost:8082/api/procesamiento/estado |
| Servicio alertas | http://localhost:8083/actuator/health |
| Streaming | http://localhost:8084/api/streaming/health |
| Kafka UI | http://localhost:8090 |
| RabbitMQ | http://localhost:15672 |
| Oracle | localhost:1522/XEPDB1 |

Para probar el BFF directamente en local con `SECURITY_MODE=GATEWAY`, añade el header configurado:

```bash
curl http://localhost:8080/api/bff/alertas \
  -H 'X-Api-Gateway-Secret: CAMBIAR_POR_UN_VALOR_LARGO_Y_ALEATORIO'
```

También puedes usar `SECURITY_MODE=PERMIT_ALL` solo para desarrollo local. En la VM debe permanecer `GATEWAY`.

## Frontend local

```bash
cd frontend-angular18-azuread
npm install
npm start
```

Abre `http://localhost:4200`. El archivo `src/environments/environment.ts` ya apunta al API Gateway del Grupo 4. Microsoft Entra ID debe tener `http://localhost:4200/` como URI de redirección SPA.

## Publicar imágenes DockerLab

```bash
docker login
export DOCKERLAB_USER=j4cobiyo
export IMAGE_TAG=3.0.1
./scripts/build_and_push_dockerlab.sh
```

Se publican:

- `j4cobiyo/bff-alertas:3.0.1`
- `j4cobiyo/servicio-alertas:3.0.1`
- `j4cobiyo/streaming-alertas:3.0.1`
- `j4cobiyo/productor-senales:3.0.1`
- `j4cobiyo/procesador-senales:3.0.1`

## VM AWS

Consulta `documentacion/DESPLIEGUE_VM_AWS.md`. El Compose de VM hace `pull` de las imágenes anteriores; no compila código en la VM.

## API Gateway

Consulta `documentacion/API_GATEWAY_AWS.md`. Se reutiliza el mismo recurso comodín `/*` y la misma URL pública. Solo se actualiza la integración hacia la IP elástica de la VM y se despliega el stage `prod`.

## Pruebas

```bash
./scripts/smoke_test.sh
```

Importa `postman/MediAlert-EFT-Grupo4.postman_collection.json` para probar health, dos colas, CRUD y streaming.

## Seguridad

- Entra ID protege la sesión del frontend.
- Spring Security protege el BFF.
- AWS API Gateway inyecta `X-Api-Gateway-Secret`; Angular no conoce ese secreto.
- No se incluyen archivos `.env` reales.
- No abras Oracle, Kafka ni RabbitMQ a todo Internet.

## Variante con Oracle Cloud externo

La entrega incluye `scripts/docker-compose.vm.oracle-cloud.yml`, que no levanta Oracle XE y conecta `servicio-alertas` a una instancia Oracle Cloud externa. Ejecute primero `scripts/schema_oracle_alertas.sql` en esa base y luego:

```bash
cd scripts
cp .env.vm.oracle-cloud.example .env.vm.oracle-cloud
nano .env.vm.oracle-cloud
./vm_pull_and_up_oracle_cloud.sh .env.vm.oracle-cloud
```

Permita desde Oracle únicamente la IP privada o pública necesaria de la VM y no exponga el puerto de base de datos a todo Internet.
