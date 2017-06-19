create table Actions(
		action_id serial,
		path text not null,
		action text not null
);
insert into Actions (path,action) values ('Path/media_pause.ahk','Media Pause');
insert into Actions (path,action) values ('Path/volume_down.ahk','Volume Down');
insert into Actions (path,action) values ('Path/volume_up.ahk','Volume Up');