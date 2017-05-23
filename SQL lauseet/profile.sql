CREATE TABLE profile(
	profile_id SERIAL  primary key  not null,
	username text not null unique,
	password text not null,
	email text not null unique
);