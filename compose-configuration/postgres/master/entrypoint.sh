#!bin/bash

set -e

echo "Initializing PostgreSQL master..."
docker-entrypoint.sh postgres & POSTGRES_PID=$!
until pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB"; do
    sleep 1
done

SUBNET=$(ip route | awk '$2 == "dev" && $3 == "eth0" {print $1; exit}')
if [ -z "$SUBNET" ]; then
    echo "ERROR: Could not determine Docker subnet"
    exit 1
fi

echo "Postgres subnet: $SUBNET"

PG_HBA="$PGDATA/pg_hba.conf"

REPLICATION_RULE="host replication ${POSTGRES_REPLICATION_USER} ${SUBNET} scram-sha-256"
if ! grep -Fxq "$REPLICATION_RULE" "$PG_HBA"; then
    echo "$REPLICATION_RULE" >> "$PG_HBA"
fi

PGPASSWORD="${POSTGRES_PASSWORD}" psql \
    -v ON_ERROR_STOP=1 \
    -U "$POSTGRES_USER" \
    -d "$POSTGRES_DB" \
    -c "DO \$\$
        BEGIN
           IF NOT EXISTS (
               SELECT FROM pg_roles WHERE rolname = '${POSTGRES_REPLICATION_USER}'
           ) THEN
               CREATE ROLE ${POSTGRES_REPLICATION_USER}
                   WITH LOGIN REPLICATION PASSWORD '${POSTGRES_REPLICATION_PASSWORD}';
           END IF;
        END
        \$\$;"

# Reload pg config
PGPASSWORD="${POSTGRES_PASSWORD}" psql \
    -v ON_ERROR_STOP=1 \
    -U "$POSTGRES_USER" \
    -d "$POSTGRES_DB" \
    -c "SELECT pg_reload_conf();"

# Backup folder for replicas
mkdir -p /var/lib/postgresql/basebackup

if [ ! -f "$BASEBACKUP_DIR/PG_VERSION" ]; then
  PGPASSWORD="$POSTGRES_REPLICATION_PASSWORD" pg_basebackup \
      -h ${POSTGRES_HOST} -D /var/lib/postgresql/basebackup -U ${POSTGRES_REPLICATION_USER} -v -P --wal-method=stream
fi

kill "$POSTGRES_PID"
wait "$POSTGRES_PID" || true

exec docker-entrypoint.sh postgres
