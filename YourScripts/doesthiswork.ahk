f5::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
ControlSend, ahk_parent, ^{Right}, ahk_class SpotifyMainWindow
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
ControlSend, ahk_parent, ^{Right}, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}
return
}

f4 & 4::
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
