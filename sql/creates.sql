CREATE DATABASE phonebok;

CREATE TABLE person(
id SMALLINT NOT NULL AUTO_INCREMENT,
lastname VARCHAR(16) NOT NULL,
name VARCHAR(70) NOT NULL,
latitude DOUBLE NOT NULL,
longitude DOUBLE NOT NULL,
elevation DOUBLE,
PRIMARY KEY(id));

CREATE TABLE email(
address VARCHAR(90) NOT NULL，
pid SMALLINT NOT NULL,
PRIMARY KEY(address),
FOREIGN KEY(pid) REFERENCES person(id));

CREATE TABLE phone(
phid SMALLINT NOT NULL AUTO_INCREMENT,
area_code SMALLINT NOT NULL,
number BIGINT NOT NULL,
pid SMALLINT NOT NULL,
PRIMARY KEY(phid),
FOREIGN KEY(pid) REFERENCES person(id));