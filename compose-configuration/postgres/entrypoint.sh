#!bin/bash

set -e

if [ -f "${PGDATA}/postgresql.conf" ]; then
    POSTGRES_MASTER_CONFIG="include = '/etc/postgresql/postgres-master.conf'"
    sed -i "\|^${POSTGRES_MASTER_CONFIG}$|d" ${PGDATA}/postgresql.conf
fi

SUBNET=$(ip route | awk '$2 == "dev" && $3 == "eth0" {print $1; exit}')
if [ -z "${SUBNET}" ]; then
    echo "ERROR: Could not determine Docker subnet"
    exit 1
fi

echo "Postgres subnet: ${SUBNET}"

export POSTGRES_SUBNET="${SUBNET}"

envsubst < /tmp/patroni.yml > /etc/patroni/patroni.yml

exec gosu postgres patroni /etc/patroni/patroni.yml
