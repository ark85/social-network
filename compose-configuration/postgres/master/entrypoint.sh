#!bin/bash

set -e

if [ ! -s "$PGDATA/PG_VERSION" ]; then
    echo "Initializing PostgreSQL master..."
    docker-entrypoint.sh postgres & POSTGRES_PID=$!
    until pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB"; do
        sleep 1
    done

    SUBNET=$(ip route | awk '
        $1 != "default" && $3 == "" {
            print $1
            exit
        }
    ')

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
        -c "CREATE ROLE ${POSTGRES_REPLICATION_USER} WITH LOGIN REPLICATION PASSWORD '${POSTGRES_REPLICATION_PASSWORD}';"

    # Reload pg config
    PGPASSWORD="${POSTGRES_PASSWORD}" psql \
        -v ON_ERROR_STOP=1 \
        -U "$POSTGRES_USER" \
        -d "$POSTGRES_DB" \
        -c "SELECT pg_reload_conf();"

    # Backup folder for replicas
    mkdir -p /var/lib/postgresql/basebackup

    PGPASSWORD="$POSTGRES_REPLICATION_PASSWORD" pg_basebackup \
        -h ${POSTGRES_HOST} -D /var/lib/postgresql/basebackup -U ${POSTGRES_REPLICATION_USER} -v -P --wal-method=stream

    kill "$POSTGRES_PID"
    wait "$POSTGRES_PID" || true
fi

exec docker-entrypoint.sh postgres
