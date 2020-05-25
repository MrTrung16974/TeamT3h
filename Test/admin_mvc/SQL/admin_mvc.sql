-- create database
CREATE SCHEMA `java1908` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
use java1908;

-- create table product
create table product(
	id int(6) unsigned primary key not null AUTO_INCREMENT,
    name nvarchar(250) not null,
    description  text null,
    price varchar(100) not null,
    star int(1) null,
	created_date datetime null,
    image varchar(200) not null
);