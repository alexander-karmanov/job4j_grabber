CREATE TABLE IF NOT EXISTS post(
	id serial primary key,
	name varchar,
	text varchar,
	link varchar unique,
	created timestamp
);