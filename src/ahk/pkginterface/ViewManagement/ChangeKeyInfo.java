package ahk.pkginterface.ViewManagement;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ChangeKeyInfo {
    public File currentScriptDisblayedFile = null;
    public Label currentScriptDisblayedLabel = null;
    public Label currentActionDisbalayedLabel = null;
    public Label currentKeyDisblayedLabel = null;
    public final HashMap<String,HashMap<String,Integer>> keysIndexInScript = new HashMap<>();
    public ChangeKeyInfo(){

    }
    public HashMap<String,Integer> getCurrentScriptInformation(){
        return keysIndexInScript.get(currentScriptDisblayedFile.getName().replaceFirst("[.][^.]+$", ""));
    }
    public ArrayList<String> getCurrentScriptKeysInArrayList(){
        HashMap<String,Integer> map = keysIndexInScript.get(currentScriptDisblayedFile.getName().replaceFirst("[.][^.]+$", ""));
        return new ArrayList<String>(map.keySet());
    }
}
