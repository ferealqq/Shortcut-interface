CREATE TABLE profile(
	profile_id SERIAL  primary key  not null,
	username text not null unique,
	password text not null,
	email text not null unique,
	scriptPaths text[] null
);

update profile set scriptpaths = array_append(scriptpaths,'notfunnypekke') where profile_id = 1;
