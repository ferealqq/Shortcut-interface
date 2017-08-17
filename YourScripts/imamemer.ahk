
Esc::
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
winactivate, ahk_class SpotifyMainWindow
ControlSend, ahk_parent, ^{Right}, ahk_class SpotifyMainWindow
WinMinimize, ahk_class SpotifyMainWindow 
DetectHiddenWindows, Off
return
}
return
}

J::
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

f8::
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
