package ahk.pkginterface.Frames.Css;

import ahk.pkginterface.ViewManagement.ComponentStorage;

import javax.swing.*;
import java.io.*;

public class ScriptWriter {
    private ComponentStorage componentStorage;

    public ScriptWriter(ComponentStorage componentArchive){
        componentStorage = componentArchive;
    }

    public String writeScriptWithPath(String nameofthescript,String choosenPath){
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            for (String choosenActionPath : componentStorage.choosenActionPath) {
                reader = new BufferedReader(new FileReader(choosenActionPath));
                File file = new File(choosenPath + "/" + nameofthescript + ".ahk");
                file.createNewFile();
                writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
                for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                    firstRow = firstRow + " & " + componentStorage.pressedKeys.get(i).getKeysynonyminahk();
                }
                writer.write(firstRow + "::");
                String sCurrentLine;
                while ((sCurrentLine = reader.readLine()) != null) {
                    writer.newLine();
                    writer.write(sCurrentLine);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(componentStorage.mainFrame, e);
        } finally {
            close(writer);
            close(reader);
        }
        return null;
    }
    private static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
        }
    }
}
