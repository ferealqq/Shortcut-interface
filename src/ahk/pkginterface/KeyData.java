package ahk.pkginterface;

import java.util.ArrayList;

public class KeyData {
    public ArrayList<String[]> rows = new ArrayList<>();
    public String[] firstrow = {"Esc","f1","f2","f3","f4","f5","f6","f7","f8","f9","f10","f11","f12"};
    public String[] secondrow = {"§","1","2","3","4","5","6","7","8","9","0","+","´","Backspace"};
    public String[] thirdrow = {"Tab","q","w","e","r","t","y","u","i","o","p","å","¨","Enter"};
    public String[] fourthrow = {"Caps","a","s","d","f","g","h","j","k","l","ö","ä","'"};
    public String[] fifthrow = {"Shift","<","z","x","c","v","b","n","m",",",".","-","Shift"};
    public String[] sixthrow = {"Ctrl","Fn","Alt","Space","AltGr","Win","Ctrl"};

    public KeyData(){
        rows.add(firstrow);
        rows.add(secondrow);
        rows.add(thirdrow);
        rows.add((fourthrow));
        rows.add(fifthrow);
        rows.add(sixthrow);
    }
}
