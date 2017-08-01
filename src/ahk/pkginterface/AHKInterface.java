package ahk.pkginterface;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;

import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class AHKInterface extends JFrame {
    public final JFXPanel ahkinterfaceView = new JFXPanel();
    public final VBox rootPane = new VBox();
    public final BorderPane stepPane = new BorderPane();
    private final ArrayList<Button> bottomRowButtons = new ArrayList<>();

    private EventHandler<ActionEvent> nextEventHandler;
    private EventHandler<ActionEvent> detectEventHandler;
    private EventHandler<ActionEvent> addactionEventHandler;

    public final JFrame main = this;
    public ComponentStorage componentStorage;// siirä viewmap aloitus formiin sitten kuin se on tehty

    public AHKInterface() {
        componentStorage = new ComponentStorage(main);
        componentStorage.setAhkinterface(this);
        componentStorage.nameofthescript = JOptionPane.showInputDialog(this,"Name your script");
        constructAHK();
    }

    private void constructAHK() {
        this.add(ahkinterfaceView);
        this.setVisible(true);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        initComponents(ahkinterfaceView);
    }

    private void initComponents(JFXPanel jfxPanel) {
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }



    /*
    * Remember to run these methods in following order or the code will not work. Because they relay on the other variables in the other methods.
    * reateListeners();
        createStepBar();
        createComponents(actionsData.getActions());
        createButtons();
        setListeners();
     */
    private Scene createScene() {
        Scene scene = new Scene(rootPane, 1000, 600);
        rootPane.getChildren().add(componentStorage.menuSetup.createMenuBar()); // poista kuin uusi mainform on tehty
        createKeyListeners();
        componentStorage.createStepBar(rootPane);
        componentStorage.highLightCurrentStep(1);
        try {
            createKeyboard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createButtons(scene);
        setKeyListeners();
        return (scene);
    }

    private void createKeyListeners() {
        nextEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (componentStorage.pressedKeys.isEmpty()) {
                    JOptionPane.showMessageDialog(AHKInterface.super.rootPane, "You havent selected any keys try again later!");
                    return;
                }
                componentStorage.hideSelectedAndShowSelected(ahkinterfaceView, componentStorage.viewMap.get("browseaction"));
            }
        };
        detectEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        };
    }

    private void setKeyListeners() {
        for (int i = 0; i < bottomRowButtons.size(); i++) {
            switch (bottomRowButtons.get(i).getText()) {
                case "Detect":
                    Button btDetect = bottomRowButtons.get(i);
                    btDetect.setOnAction(detectEventHandler);
                    break;
                case "Next":
                    Button btNext = bottomRowButtons.get(i);
                    btNext.setOnAction(nextEventHandler);
                    break;
            }
        }
    }

    private void createButtons(Scene scene) {
        HBox buttonRow = new HBox();

        Button btBack = new Button("Back to menu");
        buttonRow.setHgrow(btBack, Priority.ALWAYS);
        btBack.setMaxWidth(Double.MAX_VALUE);
        btBack.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btBack);

        Button btScripts = new Button("Your Scripts");
        buttonRow.setHgrow(btScripts, Priority.ALWAYS);
        btScripts.setMaxWidth(Double.MAX_VALUE);
        btScripts.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btScripts);

        Button btDetect = new Button("Detect");
        buttonRow.setHgrow(btDetect, Priority.ALWAYS);
        btDetect.setMaxWidth(Double.MAX_VALUE);
        btDetect.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btDetect);


        Button btBrowse = new Button("Browse Scripts");
        buttonRow.setHgrow(btBrowse, Priority.ALWAYS);
        btBrowse.setMaxWidth(Double.MAX_VALUE);
        btBrowse.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btBrowse);


        Button btNext = new Button("Next");
        buttonRow.setHgrow(btNext, Priority.ALWAYS);
        if(componentStorage.pressedKeys.isEmpty()){
            btNext.setDisable(true);
        }else{
            btNext.setDisable(false);
        }
        buttonRow.getChildren().addAll(btBack, btScripts, btDetect, btBrowse, btNext);
        buttonRow.setAlignment(Pos.BOTTOM_LEFT);
        bottomRowButtons.add(btNext);

        String buttonRowCss = this.getClass().getResource("Css/main_btns.css").toExternalForm();
        buttonRow.getStylesheets().add(buttonRowCss);
        rootPane.getChildren().add(buttonRow);
    }

    private void createKeyboard() throws FileNotFoundException {
        Keys keys = new KeyData().readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
        for (ArrayList<Key> row : keys.rows) {
            HBox rowPane = new HBox();
            for (int i = 0; i < row.size(); i++) {
                Key currentkey = row.get(i);
                Button btnKey = new Button(currentkey.getKey());
                rowPane.setHgrow(btnKey, Priority.ALWAYS);
                btnKey.setMaxWidth(Double.MAX_VALUE);
                btnKey.setMaxHeight(Double.MAX_VALUE);
                btnKey.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                         if (componentStorage.pressedKeys.contains(currentkey)) {
                           componentStorage.pressedKeys.remove(currentkey);
                           btnKey.setStyle(null);
                            if(componentStorage.pressedKeys.isEmpty()){
                                bottomRowButtons.get(bottomRowButtons.size()-1).setDisable(true);
                            }else{
                                bottomRowButtons.get(bottomRowButtons.size()-1).setDisable(false);
                            }
                        } else {
                            btnKey.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
                            componentStorage.pressedKeys.add(currentkey);
                            if(!componentStorage.pressedKeys.isEmpty()) bottomRowButtons.get(bottomRowButtons.size()-1).setDisable(false);
                         }

                    }
                });
                rowPane.getChildren().add(btnKey);
            }
            rootPane.setVgrow(rowPane, Priority.ALWAYS);
            rootPane.getChildren().add(rowPane);
        }

        /*
        * If you want something out of mainPane use this.
        Node nodeOut = mainPane.getChildren().get(1);
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
