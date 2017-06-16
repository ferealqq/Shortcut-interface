package ahk.pkginterface;

import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class AHKInterface extends JFrame {
    private final JFXPanel jfxPanel = new JFXPanel();
    private final VBox rootPane = new VBox();
    public AHKInterface(){
        this.add(jfxPanel);
        this.setVisible(true);
        this.setSize(1000,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        initComponents(jfxPanel);
    }

    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene() {
        Scene scene = new Scene(rootPane,1000,600);
        try {
            createKeyboard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createButtons();
        return (scene);
    }
    private void createButtons(){
        HBox buttonRow = new HBox();

        Button btScripts = new Button("Scripts");
        buttonRow.setHgrow(btScripts, Priority.ALWAYS);
        btScripts.setMaxWidth(Double.MAX_VALUE);
        btScripts.setMaxHeight(Double.MAX_VALUE);

        Button btSignin = new Button("Sign in");
        buttonRow.setHgrow(btSignin, Priority.ALWAYS);
        btSignin.setMaxWidth(Double.MAX_VALUE);
        btSignin.setMaxHeight(Double.MAX_VALUE);

        Button btDetect = new Button("Detect");
        buttonRow.setHgrow(btDetect, Priority.ALWAYS);
        btDetect.setMaxWidth(Double.MAX_VALUE);
        btDetect.setMaxHeight(Double.MAX_VALUE);

        Button btUndo = new Button("Undo");
        buttonRow.setHgrow(btUndo, Priority.ALWAYS);
        btUndo.setMaxHeight(Double.MAX_VALUE);
        btUndo.setMaxWidth(Double.MAX_VALUE);

        Button btBrowse = new Button("Browse");
        buttonRow.setHgrow(btBrowse,Priority.ALWAYS);
        btBrowse.setMaxWidth(Double.MAX_VALUE);
        btBrowse.setMaxHeight(Double.MAX_VALUE);

        Button btCommit = new Button("Commit");
        buttonRow.setHgrow(btCommit,Priority.ALWAYS);
        btCommit.setMaxHeight(Double.MAX_VALUE);
        btCommit.setMaxWidth(Double.MAX_VALUE);

        Button btHelp = new Button("Help");
        buttonRow.setHgrow(btHelp,Priority.ALWAYS);
        btHelp.setMaxWidth(Double.MAX_VALUE);
        btHelp.setMaxHeight(Double.MAX_VALUE);

        Button btNext = new Button("Next");
        buttonRow.setHgrow(btNext,Priority.ALWAYS);
        buttonRow.getChildren().addAll(btScripts,btSignin,btDetect,btUndo,btBrowse,btCommit,btHelp,btNext);
        buttonRow.setAlignment(Pos.BOTTOM_LEFT);
        rootPane.getChildren().add(buttonRow);
    }
    private void createKeyboard() throws FileNotFoundException {
        Keys keys = new KeyData().readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
        for (ArrayList<Key> row :keys.rows) {
            HBox rowPane = new HBox();
            for (int i = 0; i <row.size();i++) {
                Button btnKey = new Button(row.get(i).getKey());
                rowPane.setHgrow(btnKey, Priority.ALWAYS);
                btnKey.setMaxWidth(Double.MAX_VALUE);
                btnKey.setMaxHeight(Double.MAX_VALUE);
                rowPane.getChildren().add(btnKey);
            }
            rootPane.setVgrow(rowPane,Priority.ALWAYS);
            rootPane.getChildren().add(rowPane);
        }

        /*
        * If you want something out of rootPane use this.
        Node nodeOut = rootPane.getChildren().get(1);
        if(nodeOut instanceof HBox){
            for(Node nodeIn:((HBox)nodeOut).getChildren()){
                if(nodeIn instanceof Button){
                    System.out.println(((Button)nodeIn).getText());
                }
            }
        }
        */
    }
    public static void main(String[] args) {
        AHKInterface k = new AHKInterface();
    }
}
