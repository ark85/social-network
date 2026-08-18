#! bin/bash

set -e

echo "Starting PostgreSQL replica..."

BASEBACKUP_DIR="/var/lib/postgresql/basebackup"

if  [ ! -f "$PGDATA/PG_VERSION" ] || [ ! -f "$PGDATA/standby.signal" ]; then

    echo "PGDATA is empty. Waiting for base backup..."

    until [ -f "$BASEBACKUP_DIR/PG_VERSION" ]; do
        sleep 1
    done

    echo "Base backup found."

    rm -rf "${PGDATA:?}"/*
    mkdir -p "$PGDATA"
    cp -a "$BASEBACKUP_DIR"/. "$PGDATA"/
    chown -R postgres:postgres "$PGDATA"

    touch "$PGDATA/standby.signal"

    cat >> "$PGDATA/postgresql.auto.conf" <<EOF
primary_conninfo = 'host=${POSTGRES_MASTER_HOST} port=${POSTGRES_MASTER_PORT} user=${POSTGRES_REPLICATION_USER} password=${POSTGRES_REPLICATION_PASSWORD} application_name=${REPLICA_NAME}'
EOF

fi

exec docker-entrypoint.sh postgres
