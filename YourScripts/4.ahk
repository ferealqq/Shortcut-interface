
f4::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
SoundSet, +1, , mute
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
SoundSet, +1, , mute
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}

f5::
DetectHiddenWindows, On 
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

f6::
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

f8::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
SoundSet +1
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
SoundSet +1
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}
