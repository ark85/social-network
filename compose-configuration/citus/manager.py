import logging
import os
import time

import psycopg
from psycopg import OperationalError


COORDINATOR_HOST = os.environ["CITUS_COORDINATOR_HOST"]
COORDINATOR_PORT = int(os.environ.get("CITUS_COORDINATOR_PORT", "5432"))

DATABASE_NAME = os.environ["POSTGRES_DB"]
DATABASE_USER = os.environ["POSTGRES_USER"]
DATABASE_PASSWORD = os.environ["POSTGRES_PASSWORD"]

WORKER_NODES = os.environ["CITUS_WORKERS"].split(",")

logger = logging.getLogger(__name__)


def configure_logging() -> None:
    logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")


def wait_until_postgres_is_ready(host: str, port: int) -> None:
    """Wait until PostgreSQL accepts connections."""

    while True:
        try:
            with psycopg.connect(
                host=host,
                port=port,
                dbname=DATABASE_NAME,
                user=DATABASE_USER,
                password=DATABASE_PASSWORD,
                connect_timeout=2,
            ):
                logger.info("PostgreSQL is ready: %s:%s", host, port)
                return
        except OperationalError:
            logger.info("Waiting for PostgreSQL: %s:%s", host, port)
            time.sleep(2)


def is_worker_already_registered(connection: psycopg.Connection, worker_host: str, worker_port: int) -> bool:
    """Check whether a worker is already registered in Citus."""

    result = connection.execute(
        """
        SELECT EXISTS (
            SELECT 1
            FROM pg_dist_node
            WHERE nodename = %s
              AND nodeport = %s
        )
        """,
        (worker_host, worker_port),
    )

    return result.fetchone()[0]


def register_worker_if_not_registered(connection: psycopg.Connection, worker_host: str, worker_port: int) -> None:
    """Register a Citus worker if it is not registered yet."""

    worker_is_registered = is_worker_already_registered(connection, worker_host, worker_port)

    if worker_is_registered:
        logger.info("Citus worker is already registered: %s:%s", worker_host, worker_port)
    else:
        logger.info("Registering Citus worker: %s:%s", worker_host, worker_port)
        connection.execute("SELECT citus_add_node(%s, %s)", (worker_host, worker_port))


def rebalance_shards(connection: psycopg.Connection) -> None:
    """Rebalance distributed table shards across registered workers."""

    logger.info("Rebalancing distributed table shards")
    connection.execute("SELECT rebalance_table_shards()")


def register_all_citus_workers() -> None:
    """Connect to the Citus coordinator and register all configured workers."""

    with psycopg.connect(
        host=COORDINATOR_HOST,
        port=COORDINATOR_PORT,
        dbname=DATABASE_NAME,
        user=DATABASE_USER,
        password=DATABASE_PASSWORD,
    ) as connection:
        connection.execute("CREATE EXTENSION IF NOT EXISTS citus")

        for worker_node in WORKER_NODES:
            worker_host, worker_port = worker_node.split(":", 1)
            register_worker_if_not_registered(connection, worker_host, int(worker_port))

        connection.commit()
        rebalance_shards(connection)
        connection.commit()


def main() -> None:
    """Wait for the Citus cluster and register all configured workers."""

    configure_logging()
    logger.info("Starting Citus cluster configuration")

    wait_until_postgres_is_ready(COORDINATOR_HOST, COORDINATOR_PORT)

    for worker_node in WORKER_NODES:
        worker_host, worker_port = worker_node.split(":", 1)
        wait_until_postgres_is_ready(worker_host, int(worker_port))

    register_all_citus_workers()

    logger.info("Citus cluster configuration completed successfully")


if __name__ == "__main__":
    main()
