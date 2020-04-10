FROM mysql:5.7

ENV MYSQL_ROOT_PASSWORD=appuser \
    MYSQL_USER=appuser \
    MYSQL_PASSWORD=appuser \
    MYSQL_DATABASE=droptoken

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306