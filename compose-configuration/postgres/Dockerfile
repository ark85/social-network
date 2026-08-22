FROM postgres:18

RUN apt-get update \
    && apt-get install -y patroni \
    && apt-get install -y curl

COPY patroni/patroni.yml /etc/patroni/patroni.yml
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
