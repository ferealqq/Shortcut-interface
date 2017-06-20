package ahk.pkginterface;

import ahk.pkginterface.browsingFrames.browseAction;
import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class AHKInterface extends JFrame {
    public final JFXPanel mainJfxPane = new JFXPanel();
    private final VBox rootPane = new VBox();

    private final ArrayList<Button> bottomRowButtons = new ArrayList<>();
    public final ArrayList<Key> pressedKeys = new ArrayList<>();

    private EventHandler<ActionEvent> btNextAction;
    private EventHandler<ActionEvent> btDetectAction;


    public AHKInterface(){
        this.add(mainJfxPane);
        this.setVisible(true);
        this.setSize(800,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        initComponents(mainJfxPane);
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
        createButtons(scene);
        createKeyListeners();
        setKeyListeners();
        return (scene);
    }
    private void createKeyListeners(){
        AHKInterface mainFrame = this;
        btNextAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(pressedKeys.isEmpty()) {
                    JOptionPane.showMessageDialog(AHKInterface.super.rootPane,"You havent selected any keys try again later!");
                    return;
                }
                mainJfxPane.hide();
                browseAction browseJfxPane = new browseAction(mainFrame);
                add(browseJfxPane.giveView());
            }
        };
    }
    private void setKeyListeners(){
        for (int i = 0; i < bottomRowButtons.size(); i++) {
            switch(i){
                case 2:
                    Button btDetect = bottomRowButtons.get(2);
                    btDetect.setOnAction(btDetectAction);
                case 7:
                    Button btNext = bottomRowButtons.get(7);
                    btNext.setOnAction(btNextAction);
            }
        }
    }
    private void createButtons(Scene scene){
        HBox buttonRow = new HBox();

        Button btScripts = new Button("Scripts");
        buttonRow.setHgrow(btScripts, Priority.ALWAYS);
        btScripts.setMaxWidth(Double.MAX_VALUE);
        btScripts.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btScripts);

        Button btSignin = new Button("Sign in");
        buttonRow.setHgrow(btSignin, Priority.ALWAYS);
        btSignin.setMaxWidth(Double.MAX_VALUE);
        btSignin.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btSignin);

        Button btDetect = new Button("Detect");
        buttonRow.setHgrow(btDetect, Priority.ALWAYS);
        btDetect.setMaxWidth(Double.MAX_VALUE);
        btDetect.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btDetect);

        Button btUndo = new Button("Undo");
        buttonRow.setHgrow(btUndo, Priority.ALWAYS);
        btUndo.setMaxHeight(Double.MAX_VALUE);
        btUndo.setMaxWidth(Double.MAX_VALUE);
        bottomRowButtons.add(btUndo);

        Button btBrowse = new Button("Browse");
        buttonRow.setHgrow(btBrowse,Priority.ALWAYS);
        btBrowse.setMaxWidth(Double.MAX_VALUE);
        btBrowse.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btBrowse);

        Button btCommit = new Button("Commit");
        buttonRow.setHgrow(btCommit,Priority.ALWAYS);
        btCommit.setMaxHeight(Double.MAX_VALUE);
        btCommit.setMaxWidth(Double.MAX_VALUE);
        bottomRowButtons.add(btCommit);

        Button btHelp = new Button("Help");
        buttonRow.setHgrow(btHelp,Priority.ALWAYS);
        btHelp.setMaxWidth(Double.MAX_VALUE);
        btHelp.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btHelp);

        Button btNext = new Button("Next");
        buttonRow.setHgrow(btNext,Priority.ALWAYS);
        buttonRow.getChildren().addAll(btScripts,btSignin,btDetect,btUndo,btBrowse,btCommit,btHelp,btNext);
        buttonRow.setAlignment(Pos.BOTTOM_LEFT);
        bottomRowButtons.add(btNext);

        String buttonRowCss = this.getClass().getResource("Css/ahk_main_bottombtns_css.css").toExternalForm();
        buttonRow.getStylesheets().add(buttonRowCss);
        rootPane.getChildren().add(buttonRow);
    }
    private void createKeyboard() throws FileNotFoundException {
        Keys keys = new KeyData().readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
        for (ArrayList<Key> row :keys.rows) {
            HBox rowPane = new HBox();
            for (int i = 0; i <row.size();i++) {
                Key currentkey = row.get(i);
                Button btnKey = new Button(currentkey.getKey());
                rowPane.setHgrow(btnKey, Priority.ALWAYS);
                btnKey.setMaxWidth(Double.MAX_VALUE);
                btnKey.setMaxHeight(Double.MAX_VALUE);
                btnKey.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        if(pressedKeys.contains(currentkey)) {
                            pressedKeys.remove(currentkey);
                            btnKey.setStyle(null);
                            System.out.println("removed "+ currentkey.getKey());
                        }else{
                            btnKey.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
                            System.out.println("added " + currentkey.getKey());
                            pressedKeys.add(currentkey);
                        }

                    }
                });
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
