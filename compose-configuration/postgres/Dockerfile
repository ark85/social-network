FROM postgres:18

RUN apt-get update \
    && apt-get install -y patroni \
    && apt-get install -y python3-etcd \
    && apt-get install -y gettext-base \
    && apt-get install -y curl

COPY patroni/patroni.yml /tmp/patroni.yml
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
