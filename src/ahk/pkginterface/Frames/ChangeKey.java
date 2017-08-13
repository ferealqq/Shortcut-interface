package ahk.pkginterface.Frames;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChangeKey {
    public final JFXPanel changeKeyView = new JFXPanel();
    public final VBox rootPane = new VBox();
    private final ArrayList<Button> bottomRowButtons = new ArrayList<>();

    private EventHandler<ActionEvent> nextEventHandler;

    public ComponentStorage componentStorage;

    public ChangeKey(ComponentStorage compStorage) {
        componentStorage = compStorage;
        initComponents(changeKeyView);
    }
    private void initComponents(JFXPanel jfxPanel) {
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }

    private Scene createScene() {
        Scene scene = new Scene(rootPane, 1000, 600);
        try {
            createKeyboard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        createButtons(scene);
        return (scene);
    }


    private void createButtons(Scene scene) {
        HBox buttonRow = new HBox();

        Button btBack = new Button("Back to menu");
        btBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.hideSelectedAndShowSelected((JFXPanel)componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount()-1),componentStorage.viewMap.get("ahkinterface"));
            }
        });
        buttonRow.setHgrow(btBack, Priority.ALWAYS);
        btBack.setMaxWidth(Double.MAX_VALUE);
        btBack.setMaxHeight(Double.MAX_VALUE);
        bottomRowButtons.add(btBack);

        Button btChangeKey = new Button("Change Key");
        buttonRow.setHgrow(btChangeKey, Priority.ALWAYS);
        btChangeKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (componentStorage.toBeChangedKeys.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You havent selected any keys try again later!");
                    return;
                }
                String keyToBeChanged = componentStorage.changeKeyInfo.currentKeyDisblayedLabel.getText().replace(" ","");
                File scriptToChangeIn = componentStorage.changeKeyInfo.currentScriptDisblayedFile;
                HashMap<String,Integer> map = componentStorage.changeKeyInfo.keysIndexInScript.get(scriptToChangeIn.getName().replaceFirst("[.][^.]+$", ""));
                int indexWhereToBePlaced= map.get(keyToBeChanged);
                List<String> lines = null;
                try {
                    lines = Files.readAllLines(scriptToChangeIn.toPath(), StandardCharsets.UTF_8);
                    lines.remove(indexWhereToBePlaced-1);
                    lines.add(indexWhereToBePlaced-1, componentStorage.toBeChangedKeys.get(0).getKeysynonyminahk()+"::");
                    Files.write(scriptToChangeIn.toPath(), lines, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                componentStorage.hideSelectedAndShowSelected(changeKeyView, componentStorage.viewMap.get("ahkinterface"));
            }
        });
        buttonRow.getChildren().addAll(btBack, btChangeKey);
        buttonRow.setAlignment(Pos.BOTTOM_LEFT);
        bottomRowButtons.add(btChangeKey);

        String buttonRowCss = this.getClass().getResource("Css/main_btns.css").toExternalForm();
        buttonRow.getStylesheets().add(buttonRowCss);
        rootPane.getChildren().add(buttonRow);
    }

    private void createKeyboard() throws FileNotFoundException {
        Keys keys = new KeyData().readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
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
                        if (componentStorage.toBeChangedKeys.contains(currentkey)) {
                            componentStorage.toBeChangedKeys.remove(currentkey);
                            btnKey.setStyle(null);
                            if (componentStorage.toBeChangedKeys.isEmpty()) {
                                bottomRowButtons.get(bottomRowButtons.size() - 1).setDisable(true);
                            } else {
                                bottomRowButtons.get(bottomRowButtons.size() - 1).setDisable(false);
                            }
                            disableOrEnable(KeyButtonPane);
                        } else {
                            btnKey.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
                            componentStorage.toBeChangedKeys.add(currentkey);
                            if (!componentStorage.toBeChangedKeys.isEmpty())
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
        if (componentStorage.toBeChangedKeys.size() >= 2) {
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