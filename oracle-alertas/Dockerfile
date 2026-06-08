# Imagen Oracle XE para la base de datos del proyecto MediAlert.
# Se basa en gvenzl/oracle-xe porque permite inicializar el esquema con scripts SQL.
FROM gvenzl/oracle-xe:21-slim

LABEL app="medialert-oracle" \
      description="Oracle XE con esquema ALERTAS_APP para sistema de alertas medicas" \
      version="1.0.0"

COPY init/01_schema_oracle_alertas.sql /container-entrypoint-initdb.d/01_schema_oracle_alertas.sql

EXPOSE 1521
