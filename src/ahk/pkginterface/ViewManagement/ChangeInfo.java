package ahk.pkginterface.ViewManagement;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ChangeInfo {
    public File currentScriptDisblayedFile = null;
    public Label currentScriptDisblayedLabel = null;
    public Label currentActionDisbalayedLabel = null;
    public Label currentKeyDisblayedLabel = null;
    public final HashMap<String,HashMap<String,Integer>> keysIndexInScript = new HashMap<>();
    public ChangeInfo(){

    }
    public HashMap<String,Integer> getCurrentScriptKeyInfo(){
        return keysIndexInScript.get(currentScriptDisblayedFile.getName().replaceFirst("[.][^.]+$", ""));
    }
    public Integer getIndexForSpecificKey(String specificKey){
        System.out.println(getCurrentScriptKeyInfo().get(specificKey.replace(" ","")));
        return getCurrentScriptKeyInfo().get(specificKey.replace(" ",""));
    }
    public ArrayList<String> getCurrentScriptKeysInArrayList(){
        HashMap<String,Integer> map = keysIndexInScript.get(currentScriptDisblayedFile.getName().replaceFirst("[.][^.]+$", ""));
        return new ArrayList<String>(map.keySet());
    }
}
