#!/usr/bin/env bash
set -Eeuo pipefail
BOOTSTRAP_SERVER="${KAFKA_BOOTSTRAP_SERVER:-kafka1:29092}"
MAX_ATTEMPTS="${KAFKA_INIT_MAX_ATTEMPTS:-60}"
SLEEP_SECONDS="${KAFKA_INIT_SLEEP_SECONDS:-5}"
create_topic(){
  local topic="$1"
  for ((i=1;i<=MAX_ATTEMPTS;i++)); do
    if kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --create --if-not-exists --topic "$topic" --partitions 3 --replication-factor 3; then
      echo "[kafka-init] $topic listo"; return 0
    fi
    sleep "$SLEEP_SECONDS"
  done
  echo "[kafka-init] No se pudo crear $topic" >&2; return 1
}
for ((i=1;i<=MAX_ATTEMPTS;i++)); do
  kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --list >/dev/null 2>&1 && break
  sleep "$SLEEP_SECONDS"
done
create_topic "senales_vitales"
create_topic "alertas"
create_topic "alertas-medicas"
create_topic "resumenes-vitales"
kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" --list
