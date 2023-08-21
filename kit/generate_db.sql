CREATE ROLE admin WITH
    LOGIN
    SUPERUSER
    CREATEDB
    CREATEROLE
    INHERIT
    REPLICATION
    CONNECTION LIMIT -1
    PASSWORD '1234';

DROP DATABASE db_example1;

CREATE DATABASE db_example1
    WITH
    OWNER = root
    ENCODING = 'UTF8'
    TABLESPACE = pg_default;

CREATE SCHEMA IF NOT EXISTS sc_example AUTHORIZATION root;

SET search_path TO sc_example;

CREATE TABLE pollution (
    id bigint PRIMARY KEY NOT NULL,
    city varchar(20) NOT NULL,
    day date NOT NULL ,
    co varchar(20) NOT NULL,
    so2 varchar(20) NOT NULL ,
    o3 varchar(20) NOT NULL,
    FOREIGN KEY (city) REFERENCES cities(name)
);

CREATE TABLE cities (
    name varchar(20) PRIMARY KEY NOT NULL ,
    lat float(24),
    lon float(24)
)