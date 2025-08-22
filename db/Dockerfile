# Dockerfile for the custom docker of the Database
# Base image
FROM postgres:17-bookworm

# Copy the sql script we wann use over
COPY ./setup_scripts/create_inb_tables.sql ./docker-entrypoint-initdb.d
COPY ./setup_scripts/create_inb_stage_tables.sql ./docker-entrypoint-initdb.d





