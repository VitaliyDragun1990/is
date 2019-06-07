# Derived from official postgres image (our base image)
FROM postgres:9.6

# Add a password for root
ENV POSTGRES_USER=ishop \
    POSTGRES_PASSWORD=ishop \
    POSTGRES_DB=ishop

# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
COPY ./sql-scripts/ /docker-entrypoint-initdb.d/
