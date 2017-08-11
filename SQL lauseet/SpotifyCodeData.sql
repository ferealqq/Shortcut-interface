create table SpotifyCodeData(
	CodeData text[] not null
);
insert into SpotifyCodeData (CodeData) values ('{"DetectHiddenWindows, On ","IfWinActive, ahk_class SpotifyMainWindow ","\{","DetectHiddenWindows, Off","return","\}","IfWinNotActive, ahk_class SpotifyMainWindow","\{","WinShow, ahk_class SpotifyMainWindow","winactivate, ahk_class SpotifyMainWindow","WinMinimize, ahk_class SpotifyMainWindow ","DetectHiddenWindows, Off","return","\}"}');