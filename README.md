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

## How to migrate dialog messages from Citus to Tarantool

* Start Citus and Tarantool before the rest of the application
  * Call `docker compose -f ./docker-compose.yml -p social-network --profile dialog-migration up -d --build citus-coordinator citus-worker-1 citus-worker-2 tarantool`
* Open the Tarantool console
  * Call `MSYS_NO_PATHCONV=1 docker compose -f ./docker-compose.yml -p social-network exec -it tarantool tt connect /var/lib/tarantool/console.sock`
    * `MSYS_NO_PATHCONV=1` keeps Git Bash from rewriting `/var/lib/tarantool/console.sock`
* Or open a shell in the container and connect from there
  * Call `docker compose -f ./docker-compose.yml -p social-network exec -it tarantool sh`
  * Call `tt connect /var/lib/tarantool/console.sock`
* Import dialog messages
  * Call `import_dialog_messages()`
    * Use `import_dialog_messages(500)` to set another batch size
* Start the application
  * Call `docker compose -f ./docker-compose.yml -p social-network up -d`
