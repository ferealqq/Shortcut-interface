
f5::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
ControlSend, ahk_parent, ^{Left}, ahk_class SpotifyMainWindow
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
ControlSend, ahk_parent, ^{Left}, ahk_class SpotifyMainWindow
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}

f6::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
ControlSend, ahk_parent, ^{Up}, ahk_class SpotifyMainWindow
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
ControlSend, ahk_parent, ^{Up}, ahk_class SpotifyMainWindow
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}

f7::
DetectHiddenWindows, On 
IfWinActive, ahk_class SpotifyMainWindow 
{
ControlSend, ahk_parent, ^{Down}, ahk_class SpotifyMainWindow
DetectHiddenWindows, Off
return
}
IfWinNotActive, ahk_class SpotifyMainWindow
{
WinShow, ahk_class SpotifyMainWindow
winactivate, ahk_class SpotifyMainWindow
ControlSend, ahk_parent, ^{Down}, ahk_class SpotifyMainWindow
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}
