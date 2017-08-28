package ahk.pkginterface.Frames;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;


public class KeySelection {
    public final JFXPanel keySelectionView = new JFXPanel();
    public final VBox rootPane = new VBox();
    private final ArrayList<Button> bottomRowButtons = new ArrayList<>();

    private EventHandler<ActionEvent> nextEventHandler;
    private EventHandler<ActionEvent> detectEventHandler;
    private EventHandler<ActionEvent> yourScriptEventHandler;
    private EventHandler<ActionEvent> backtomenuEventHandler;

    public ComponentStorage componentStorage;

    public KeySelection(ComponentStorage compStorage) {
        componentStorage = compStorage;
        initComponents(keySelectionView);
    }
    private void initComponents(JFXPanel jfxPanel) {
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }

    private Scene createScene() {
        Scene scene = new Scene(rootPane, 1000, 600);
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
    public void changeKeyMode(Boolean changeBoolean){
        if(changeBoolean){

        }
    }

    private void createKeyListeners() {
        nextEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (componentStorage.pressedKeys.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You havent selected any keys try again later!");
                    return;
                }
                componentStorage.hideSelectedAndShowSelected(keySelectionView, componentStorage.viewMap.get("browseaction"));
            }
        };
        detectEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        };
        backtomenuEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.hideSelectedAndShowSelected((JFXPanel)componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount()-1),componentStorage.viewMap.get("shortcutinterface"));
            }
        };
    }

    private void setKeyListeners() {
        for (int i = 0; i < bottomRowButtons.size(); i++) {
            switch (bottomRowButtons.get(i).getText()) {
                case "Back to menu":
                    Button btBack = bottomRowButtons.get(i);
                    btBack.setOnAction(backtomenuEventHandler);
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


        Button btNext = new Button("Next");
        buttonRow.setHgrow(btNext, Priority.ALWAYS);
        if (componentStorage.pressedKeys.isEmpty()) {
            btNext.setDisable(true);
        } else {
            btNext.setDisable(false);
        }
        buttonRow.getChildren().addAll(btBack, btNext);
        buttonRow.setAlignment(Pos.BOTTOM_LEFT);
        bottomRowButtons.add(btNext);

        String buttonRowCss = this.getClass().getResource("Css/main_btns.css").toExternalForm();
        buttonRow.getStylesheets().add(buttonRowCss);
        rootPane.getChildren().add(buttonRow);
    }

    private void createKeyboard() throws FileNotFoundException {
        Keys keys = new KeyData().getRowsUsToKeys();
        VBox KeyButtonPane = new VBox();
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
                            if (componentStorage.pressedKeys.isEmpty()) {
                                bottomRowButtons.get(bottomRowButtons.size() - 1).setDisable(true);
                            } else {
                                bottomRowButtons.get(bottomRowButtons.size() - 1).setDisable(false);
                            }
                            disableOrEnable(KeyButtonPane);
                        } else {
                            btnKey.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
                            componentStorage.pressedKeys.add(currentkey);
                            if (!componentStorage.pressedKeys.isEmpty())
                                bottomRowButtons.get(bottomRowButtons.size() - 1).setDisable(false);
                            disableOrEnable(KeyButtonPane);
                        }
                    }
                });
                rowPane.getChildren().add(btnKey);
            }
            KeyButtonPane.setVgrow(rowPane, Priority.ALWAYS);
            KeyButtonPane.getChildren().add(rowPane);
        }
        rootPane.setVgrow(KeyButtonPane, Priority.ALWAYS);
        rootPane.getChildren().add(KeyButtonPane);
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

    private void disableOrEnable(Pane keyButtonPane) {
        if (componentStorage.pressedKeys.size() >= 2) {
            for (Node node : keyButtonPane.getChildren()) {
                if (node.getClass().equals(HBox.class)) {
                    HBox hbox = (HBox) node;
                    for (Node node2 : hbox.getChildren()) {
                        if (node2.getClass().equals(Button.class) && !node2.getStyle().equals("-fx-background-color: slateblue; -fx-text-fill: white;")) {
                            node2.setDisable(true);
                        }
                    }
                }
            }
        } else {
            for (Node node : keyButtonPane.getChildren()) {
                if (node.getClass().equals(HBox.class)) {
                    HBox hbox = (HBox) node;
                    for (Node node2 : hbox.getChildren()) {
                        if (node2.getClass().equals(Button.class) && !node2.getStyle().equals("-fx-background-color: slateblue; -fx-text-fill: white;")) {
                            node2.setDisable(false);
                        }
                    }
                }
            }
        }
    }
}
