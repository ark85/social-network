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
