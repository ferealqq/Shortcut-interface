create table Actions(
		action_id serial,
		path text not null,
		action text not null,
		actionCode text[] not null,
		keyWords text[] null
);

insert into Actions (path,actionCode,action) values ('Path/mute_volume.ahk','{"SoundSet, +1, , mute"}','Mute Volume');insert into Actions (path,action,actionCode,keyWords) values ('Path/media_pause.ahk','Media Pause','{"Send \{Media_Play_Pause\}"}','{"Music","Pause","Stop","Media"}');
insert into Actions (path,actionCode,action) values ('Path/volume_down.ahk','{SoundSet -1}','Volume Down');
insert into Actions (path,actionCode,action) values ('Path/volume_up.ahk','{SoundSet +1}','Volume Up');
insert into Actions (path,actionCode,action) values ('Path/spotify_previous.ahk','{"ControlSend, ahk_parent, ^\{Left\}, ahk_class SpotifyMainWindow"}','Spotify Previous');
insert into Actions (path,actionCode,action) values ('Path/spotify_volume_up.ahk','{"ControlSend, ahk_parent, ^\{Up\}, ahk_class SpotifyMainWindow"}','Spotify Volume Up');
insert into Actions (path,actionCode,action) values ('Path/spotify_volume_down.ahk','{"ControlSend, ahk_parent, ^\{Down\}, ahk_class SpotifyMainWindow"}','Spotify Volume Down');
insert into Actions (path,actionCode,action) values ('Path/spotify_next.ahk','{"ControlSend, ahk_parent, ^\{Right\}, ahk_class SpotifyMainWindow"}','Spotify Next');