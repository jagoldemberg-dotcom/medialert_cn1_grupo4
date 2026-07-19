#!/usr/bin/env bash
set -Eeuo pipefail
API="${1:-https://3lj8j5yzki.execute-api.us-east-1.amazonaws.com/prod/api/bff}"
echo "== Health =="; curl -fsS "$API/health"; echo
echo "== Encolar señal =="; curl -fsS -X POST "$API/colas/senales" -H 'Content-Type: application/json' -d '{"pacienteRut":"12.345.678-9","pacienteNombre":"Paciente Demo","tipoSigno":"FRECUENCIA_CARDIACA","valor":135,"unidad":"lpm","umbralMinimo":60,"umbralMaximo":100,"observacion":"Smoke test"}'; echo
sleep 3
echo "== Alertas =="; curl -fsS "$API/alertas"; echo
echo "== Streaming =="; curl -fsS "$API/streaming/estadisticas"; echo
