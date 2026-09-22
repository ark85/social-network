## How to start

* Fill `.env` file (see `.env.example`)
  * Generate `JWT_SECRET` by e.g. `Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded())` (or any other way)
* Call `docker compose -f ./docker-compose.yml -p social-network up -d`
  * Add `--build` option to recreate image after application changes

## How to add users to test search api

* Call `/users/import` api
  * Use multipart with key `file` and value file `people.v2.csv`

## How to migrate to patroni

* First init of the postgres-cluster
  * Call `docker compose -f ./docker-compose.yml -p social-network up -d etcd postgres-1`
    * Need to "register" old volume `postgres_data` as primary
  * Call `docker compose -f ./docker-compose.yml -p social-network up -d postgres-2 postgres-3`
    * Replicate old volume `postgres_data` to replicas
  * Call `docker compose -f ./docker-compose.yml -p social-network up -d haproxy backend`
* Next time just follow [How to start](#how-to-start)

## How to add posts to test posts api

* Call `/post/import` api
  * Use multipart with key `file` and value file `posts.txt`

## How to rebalance Citus shards without downtime

### Add a worker

* Add `citus-worker-3` in `docker-compose.yml` (see `citus-worker-1` and `citus-worker-2` as examples)
* Add `citus-worker-3` to `citus-manager`:
  * `citus-worker-3:5432` to `CITUS_WORKERS`
  * `citus-worker-3` to `depends_on`
* Start the new worker, then run `citus-manager`:
```bash
docker compose -f ./docker-compose.yml -p social-network up -d citus-worker-3
docker compose -f ./docker-compose.yml -p social-network up --build citus-manager
```
