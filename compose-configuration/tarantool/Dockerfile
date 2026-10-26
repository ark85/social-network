FROM tarantool/tarantool:3.3.5

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates \
        cmake \
        gcc \
        g++ \
        git \
        libpq-dev \
        make \
    && git clone --depth 1 https://github.com/tarantool/pg.git /tmp/pg \
    && cmake -S /tmp/pg -B /tmp/pg/build -DCMAKE_BUILD_TYPE=RelWithDebInfo \
    && cmake --build /tmp/pg/build --target install \
    && rm -rf /tmp/pg \
    && apt-get install -y --no-install-recommends libpq5 \
    && apt-get purge -y cmake gcc g++ git make libpq-dev \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

USER tarantool
