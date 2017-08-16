package ahk.pkginterface.Frames;
import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.Actions;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.TextField;

public class ChangeAction{
    public final JFXPanel changeActionView = new JFXPanel();
    public final BorderPane rootPane = new BorderPane();


    private final Button btChangeAction = new Button("Change Actions");
    private final TextField searchField = new TextField("Search");
    private final ActionsData actionsData = new ActionsData();
    public final ComponentStorage componentStorage;
    private ArrayList<Label> currentComponentArchive = new ArrayList<>();
    public final VBox labelPane = new VBox(5);
    public final VBox topPane = new VBox(5);


    private EventHandler<ActionEvent> EventHandlerChangeAction;
    private ChangeListener<Boolean> focusListener;
    private EventHandler<KeyEvent> keyReleasedAL;
    private EventHandler<ActionEvent> btBackAction;
    public ChangeAction(ComponentStorage viewArchive) {
        componentStorage = viewArchive;
        initComponents(changeActionView);
    }
    private Scene createScene() {
        Scene scene = new Scene(rootPane,800,500);
        createListeners();
        createComponents(actionsData.getActions());
        createButtons();
        setListeners();
        rootPane.setTop(topPane);
        return (scene);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private void setListeners(){
        searchField.setOnKeyReleased(keyReleasedAL);
        searchField.focusedProperty().addListener(focusListener);
    }
    public void createListeners(){
        EventHandlerChangeAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File scriptToChangeIn = componentStorage.changeKeyInfo.currentScriptDisblayedFile;
                HashMap<String,Integer> map = componentStorage.changeKeyInfo.keysIndexInScript.get(scriptToChangeIn.getName().replaceFirst("[.][^.]+$", ""));
                componentStorage.hideSelectedAndShowSelected((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount()-1), componentStorage.viewMap.get("ahkinterface"));
            }
        };
        focusListener = new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue)
                    searchField.setText("");
            }
        };

        keyReleasedAL = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                for (Label removethis:currentComponentArchive) {
                    labelPane.getChildren().remove(removethis);
                }
                currentComponentArchive.removeAll(currentComponentArchive);
                createComponents(actionsData.searchAction(searchField.getText()));
            }
        };
    }
    public JFXPanel giveView(){
        return changeActionView;
    }
    private void createButtons(){
        BorderPane bottomPane = new BorderPane();
        String searchFieldCss = this.getClass().getResource("Css/search_field_css.css").toExternalForm();
        String btCss = this.getClass().getResource("Css/main_btns.css").toExternalForm();
        searchField.getStylesheets().add(searchFieldCss);
        topPane.getChildren().add(searchField);
        BorderPane.setAlignment(btChangeAction, Pos.BOTTOM_RIGHT);
        btChangeAction.getStylesheets().add(btCss);
        btChangeAction.setOnAction(EventHandlerChangeAction);
        bottomPane.setRight(btChangeAction);

        rootPane.setBottom(bottomPane);

    }
    private void createComponents(ArrayList<Actions> listofActions){
        ScrollPane scrollPane = new ScrollPane();
        String actionlabelsCss = this.getClass().getResource("Css/action_labels_css.css").toExternalForm();
        for(Actions action : listofActions){
            Label actionLabel = new Label();
            labelPane.setVgrow(actionLabel,Priority.ALWAYS);
            actionLabel.setText(action.getAction());
            actionLabel.getStylesheets().add(actionlabelsCss);
            actionLabel.setMaxWidth(Double.MAX_VALUE);
            actionLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!componentStorage.toBeChangedAction.contains(action.getAction())){
                        componentStorage.toBeChangedAction.add(action.getAction());
                        actionLabel.setStyle("-fx-background-color: #404040;");
                    }else{
                        componentStorage.toBeChangedAction.remove(action.getAction());
                        actionLabel.setStyle("-fx-background-color: transparent;");
                    }
                }
            });
            currentComponentArchive.add(actionLabel);
        }
        labelPane.getChildren().addAll(currentComponentArchive);
        scrollPane.setContent(labelPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        labelPane.minWidthProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        rootPane.setCenter(scrollPane);
    }
}


