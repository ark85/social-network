FROM postgres:18

COPY postgres-master.conf /etc/postgresql/postgres-master.conf
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh

RUN echo "include '/etc/postgresql/postgres-master.conf'" >> /usr/share/postgresql/postgresql.conf.sample

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["postgres"]
