# Oracle personalizado no usado en la demo final

La demo final usa directamente la imagen pública:

```text
gvenzl/oracle-xe:21-slim
```

en `scripts/docker-compose.vm.yml`.

El microservicio crea la tabla automáticamente con JPA `update`, por eso no es necesario construir ni subir una imagen Oracle personalizada.
