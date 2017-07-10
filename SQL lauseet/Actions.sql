create table Actions(
		action_id serial,
		path text not null,
		action text not null,
		keyWords text[] null
);
insert into Actions (path,action,keyWords) values ('Path/media_pause.ahk','Media Pause','{"Music","Pause","Stop","Media"}');
insert into Actions (path,action) values ('Path/volume_down.ahk','Volume Down');
insert into Actions (path,action) values ('Path/volume_up.ahk','Volume Up');
insert into Actions (path,action) values ('Path/spotify_volume_up.ahk','Spotify Volume Up');
insert into Actions (path,action) values ('Path/spotify_volume_down.ahk','Spotify Volume Down');
insert into Actions (path,action) values ('Path/spotify_next.ahk','Spotify Next');
insert into Actions (path,action) values ('Path/spotify_previous.ahk','Spotify Previous');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mic_mute.ahk','Microphone mute');
insert into Actions (path,action) values ('Path/.ahk','Mute Volume');
insert into Actions (path,action,keyWords) values ('Path/spotify_pause.ahk','Spotify Pause','{"Pause","Media","Music","Stop"}');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');
insert into Actions (path,action) values ('Path/mute_volume.ahk','Mute Volume');

