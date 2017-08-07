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
    public final BorderPane stepPane = new BorderPane();
    private final ArrayList<Button> bottomRowButtons = new ArrayList<>();

    private EventHandler<ActionEvent> nextEventHandler;
    private EventHandler<ActionEvent> detectEventHandler;
    private EventHandler<ActionEvent> yourScriptEventHandler;

    private YourScriptFrame yourScriptFrame = new YourScriptFrame();
    public ComponentStorage componentStorage;// siirä viewmap aloitus formiin sitten kuin se on tehty

    public KeySelection(ComponentStorage compStorage) {
        componentStorage = compStorage;
        initComponents(keySelectionView);
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
                    JOptionPane.showMessageDialog(null, "You havent selected any keys try again later!");
                    return;
                }
                componentStorage.hideSelectedAndShowSelected(keySelectionView, componentStorage.viewMap.get("browseaction"));
            }
        };
        yourScriptEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                yourScriptFrame.setLocation(componentStorage.ahkinterface.getX() + 200, componentStorage.ahkinterface.getY() + 75);
                yourScriptFrame.setVisible(true);
                yourScriptFrame.createLabel();
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
                case "Your Scripts":
                    Button btScript = bottomRowButtons.get(i);
                    btScript.setOnAction(yourScriptEventHandler);
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
        if (componentStorage.pressedKeys.isEmpty()) {
            btNext.setDisable(true);
        } else {
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
    class YourScriptFrame extends JFrame {
        public final JFXPanel yourScriptView = new JFXPanel();

        public final ScrollPane scrollPane = new ScrollPane();
        public final VBox vBox = new VBox(10);

        public YourScriptFrame() {
            this.setUndecorated(true);
            this.setSize(400, 400);
            initComponent();
        }

        private void initComponent() {
            Scene scene = createLocalScene();
            this.yourScriptView.setScene(scene);
            this.add(yourScriptView);
        }

        private Scene createLocalScene() {
            Scene scene = new Scene(scrollPane);
            return scene;
        }

        private void createLabel() {
            for (String path : componentStorage.oldScriptPaths) {
                Label currentlabel = new Label(path);
                vBox.setVgrow(currentlabel, Priority.ALWAYS);
                vBox.getChildren().add(currentlabel);
            }
            int oldsize = componentStorage.oldScriptPaths.size();
            scrollPane.setContent(vBox);
            vBox.minWidthProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        }
    }
}
