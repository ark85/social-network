FROM postgres:18

COPY postgres-master.conf /etc/postgresql/postgres-master.conf
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh

RUN echo "include '/etc/postgresql/postgres-master.conf'" >> /usr/share/postgresql/postgresql.conf.sample

RUN apt-get update && apt-get install -y iproute2

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["postgres"]
