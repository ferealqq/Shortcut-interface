package ahk.pkginterface.browsingFrames;

import ahk.pkginterface.AHKInterface;
import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.Actions;
import ahk.pkginterface.commentFrames.commentFrame;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private final Button btNext = new Button("Next");
    private final TextField searchField = new TextField("Search");
    private final ActionsData actionsData = new ActionsData();
    private ArrayList<Label> currentComponentArchive = new ArrayList<>();
    public VBox labelPane = new VBox(5);

    private ChangeListener<Boolean> focusListener;
    private EventHandler<KeyEvent> keyReleasedAL;
    private EventHandler<ActionEvent> btBackAction;
    public browseAction(AHKInterface mainForm) {
        mainFrame = mainForm;
        mainFrame.add(new JScrollPane(jfxPanel));
        initComponents(jfxPanel);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene(  ) {
        Scene scene = new Scene(rootPane,800,500);
        createComponents(actionsData.getActions());
        createButtons();
        createListeners();
        setListeners();
        return (scene);
    }
    private void setListeners(){
        btBack.setOnAction(btBackAction);
        searchField.setOnKeyReleased(keyReleasedAL);
        searchField.focusedProperty().addListener(focusListener);
    }
    public void createListeners(){
        focusListener = new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
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
    private void createButtons(){
        BorderPane bottomPane = new BorderPane();
        String searchFieldCss = this.getClass().getResource("search_field_css.css").toExternalForm();
        String btCss = this.getClass().getResource("ahk_main_bottombtns_css.css").toExternalForm();
        searchField.getStylesheets().add(searchFieldCss);
        rootPane.setTop(searchField);

        BorderPane.setAlignment(btNext, Pos.BOTTOM_RIGHT);
        btNext.getStylesheets().add(btCss);
        bottomPane.setRight(btNext);


        BorderPane.setAlignment(btBack, Pos.BOTTOM_LEFT);
        btBack.getStylesheets().add(btCss);
        bottomPane.setLeft(btBack);
        rootPane.setBottom(bottomPane);

    }
    private void createComponents(ArrayList<Actions> listofActions){
        ScrollPane scrollPane = new ScrollPane();
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
        scrollPane.setContent(labelPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        labelPane.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        rootPane.setCenter(scrollPane);
    }

    public static void main(String[] args) {
    }
}
