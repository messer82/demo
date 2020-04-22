CREATE DATABASE mobile_banking_transactions;

CREATE SCHEMA transactions_schema;

SET SEARCH_PATH = "transactions_schema";

CREATE TABLE mobile_banking_users (user_id SERIAL PRIMARY KEY ,
user_name varchar (256), email varchar (32), birth_date date);

CREATE TABLE accounts (account_id SERIAL PRIMARY KEY , user_id int REFERENCES mobile_banking_user(user_id),
account_no varchar(24), account_balance double precision);

CREATE TABLE transactions (transaction_id SERIAL PRIMARY KEY , account_id int REFERENCES account(account_id),
destination_account varchar (24), amount_paid double precision , transaction_date date);

