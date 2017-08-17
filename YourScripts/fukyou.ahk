
f4 & f3::
IfWinActive, ahk_class SpotifyMainWindow 
{
Send {Media_Play_Pause}
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
Send {Media_Play_Pause}
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}

f5 & f4::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
SoundSet -1
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
SoundSet -1
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}

f7::
Send {Media_Play_Pause}
