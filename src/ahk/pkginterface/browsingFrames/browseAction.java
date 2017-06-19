package ahk.pkginterface.browsingFrames;

import ahk.pkginterface.AHKInterface;
import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.Actions;
import ahk.pkginterface.commentFrames.commentFrame;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javax.swing.*;
import java.util.ArrayList;
import javafx.scene.control.TextField;

public class browseAction{
    public final JFXPanel jfxPanel = new JFXPanel();
    public final BorderPane rootPane = new BorderPane();
    public AHKInterface mainFrame;

    private final Button btBack = new Button("Back");
    private final TextField searchField = new TextField("Search");
    private final ActionsData actionsData = new ActionsData();
    private ArrayList<Label> currentComponentArchive = new ArrayList<>();

    private EventHandler<KeyEvent> keyReleasedAL;
    private EventHandler<ActionEvent> btBackAction;
    public browseAction(AHKInterface mainForm) {
        mainFrame = mainForm;
        mainFrame.add(jfxPanel);
        initComponents(jfxPanel);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene() {
        Scene scene = new Scene(rootPane,800,500);
        String searchFieldCss = this.getClass().getResource("search_field_css.css").toExternalForm();
        searchField.getStylesheets().add(searchFieldCss);
        rootPane.setTop(searchField);
        BorderPane.setAlignment(btBack, Pos.BOTTOM_LEFT);
        String btBackCss = this.getClass().getResource("ahk_main_bottombtns_css.css").toExternalForm();
        btBack.getStylesheets().add(btBackCss);
        rootPane.setBottom(btBack);
        createComponents(actionsData.getActions());
        createListeners();
        setListeners();
        return (scene);
    }
    private void setListeners(){
        btBack.setOnAction(btBackAction);
    }
    public void createListeners(){
        keyReleasedAL = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                System.out.println("works");
            }
        };
        btBackAction = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                jfxPanel.hide();
                mainFrame.mainJfxPane.show();
            }
        };
    }
    public JFXPanel giveView(){
        return jfxPanel;
    }
    private void createComponents(ArrayList<Actions> listofActions){
        VBox labelPane = new VBox(5);
        String actionlabelsCss = this.getClass().getResource("action_labels_css.css").toExternalForm();
        for(Actions action : listofActions){
            Label actionLabel = new Label();
            labelPane.setVgrow(actionLabel,Priority.ALWAYS);
            actionLabel.setText(action.getAction());
            actionLabel.getStylesheets().add(actionlabelsCss);
            actionLabel.setMaxWidth(Double.MAX_VALUE);
            currentComponentArchive.add(actionLabel);
        }
        labelPane.getChildren().addAll(currentComponentArchive);
        rootPane.setCenter(labelPane);
    }

    public static void main(String[] args) {
    }
}
