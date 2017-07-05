package ahk.pkginterface.browsingFrames;

import ahk.pkginterface.MenuSetup;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import javafx.scene.control.TextField;

public class browseAction{
    public final JFXPanel browseActionView = new JFXPanel();
    public final BorderPane rootPane = new BorderPane();
    public final BorderPane stepPane = new BorderPane();


    private final Button btBack = new Button("Back");
    private final Button btNext = new Button("Next");
    private final TextField searchField = new TextField("Search");
    private final ActionsData actionsData = new ActionsData();
    private MenuSetup menuSetup;

    private ArrayList<Label> currentComponentArchive = new ArrayList<>();
    public final VBox labelPane = new VBox(5);
    public final VBox topPane = new VBox(5);


    private EventHandler<ActionEvent> btNextAction;
    private ChangeListener<Boolean> focusListener;
    private EventHandler<KeyEvent> keyReleasedAL;
    private EventHandler<ActionEvent> btBackAction;
    public browseAction(MenuSetup menu) {
        menuSetup = menu;
        initComponents(browseActionView);
    }
    /*
    * Remember to run these methods in following order or the code will not work. Because they relay on the other variables in the other methods.
    * menuSetup.setRootPane(topPane);
    * createListeners();
        createStepBar();
        createComponents(actionsData.getActions());
        createButtons();
        setListeners();
     */
    private Scene createScene() {
        Scene scene = new Scene(rootPane,800,500);
        createListeners();
        createStepBar();
        createComponents(actionsData.getActions());
        createButtons();
        setListeners();
        return (scene);
    }
    private void createStepBar(){
        HBox centeredHBox = new HBox(35);
        Button firstStep = new Button("1");
        firstStep.setOnAction(btBackAction);
        Button secondStep = new Button("2");
        Button thirdStep = new Button("3");
        thirdStep.setOnAction(btNextAction);
        centeredHBox.getChildren().addAll(firstStep,secondStep,thirdStep);
        centeredHBox.setAlignment(Pos.CENTER);
        String stepPaneCss = this.getClass().getResource("stepPane.css").toExternalForm();
        stepPane.getStylesheets().add(stepPaneCss);
        secondStep.setStyle("-fx-background-color:#A9A9A9");
        stepPane.setCenter(centeredHBox);
        stepPane.setStyle("-fx-background-color:#F5F5F5");
        menuSetup.setRootPane(topPane);
        topPane.getChildren().add(stepPane);
        rootPane.setTop(topPane);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private void setListeners(){
        btBack.setOnAction(btBackAction);
        searchField.setOnKeyReleased(keyReleasedAL);
        searchField.focusedProperty().addListener(focusListener);
    }
    public void createListeners(){
        btNextAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // eteen päin fukin dummy
            }
        };
        btBackAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                menuSetup.hideSelectedAndShowSelected(browseActionView,menuSetup.getLastView());
            }
        };
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
                System.out.println(ke.getEventType());
                System.out.println(ke.getCode());
                for (Label removethis:currentComponentArchive) {
                    labelPane.getChildren().remove(removethis);
                }
                currentComponentArchive.removeAll(currentComponentArchive);
                createComponents(actionsData.searchAction(searchField.getText()));
            }
        };
    }
    public JFXPanel giveView(){
        return browseActionView;
    }
    private void createButtons(){
        BorderPane bottomPane = new BorderPane();
        String searchFieldCss = this.getClass().getResource("search_field_css.css").toExternalForm();
        String btCss = this.getClass().getResource("main_btns.css").toExternalForm();
        searchField.getStylesheets().add(searchFieldCss);
        topPane.getChildren().add(searchField);

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
