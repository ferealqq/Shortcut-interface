package ahk.pkginterface.ViewManagement.Css;

import javafx.scene.control.Label;

import java.io.File;
import java.util.HashMap;

public class ChangeKeyInfo {
    public File currentScriptDisblayedFile = null;
    public Label currentScriptDisblayedLabel = null;
    public Label currentActionDisbalayedLabel = null;
    public Label currentKeyDisblayedLabel = null;
    public final HashMap<String,HashMap<String,Integer>> keysIndexInScript = new HashMap<>();
    public ChangeKeyInfo(){

    }
}
