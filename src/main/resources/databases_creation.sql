CREATE DATABASE mobile_banking_transactions;

CREATE TABLE mobile_banking_users (user_id SERIAL PRIMARY KEY ,
user_name varchar (256), email varchar (32), birth_date date);
ALTER SEQUENCE mobile_banking_users_user_id_seq START 1 INCREMENT 1;

CREATE TABLE accounts (account_id SERIAL PRIMARY KEY , user_id int REFERENCES mobile_banking_users(user_id),
account_no varchar(24) UNIQUE , account_balance double precision);
ALTER SEQUENCE accounts_account_id_seq START 1 INCREMENT 1;

CREATE TABLE transactions (transaction_id SERIAL PRIMARY KEY , account_id int REFERENCES accounts(account_id),
destination_account varchar (24), amount_paid double precision, transaction_date timestamp DEFAULT now());
ALTER SEQUENCE transactions_transaction_id_seq START 1 INCREMENT 1;

