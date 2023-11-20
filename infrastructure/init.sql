CREATE DATABASE test;

USE test;

DROP TABLE IF EXISTS users;

DROP TABLE IF EXISTS crypto_prices;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    telegram_id VARCHAR(32) UNIQUE NOT NULL,
    start_time TIMESTAMP NOT NULL
);

CREATE TABLE crypto_prices (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    price DECIMAL NOT NULL,
    timestamp TIMESTAMP
);