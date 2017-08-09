package ahk.pkginterface;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Actions;
import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javax.swing.*;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class AHKInterface extends JFrame {
    public final JFXPanel ahkinterfaceView = new JFXPanel();
    public final VBox menuPaneAkaRealRootPane = new VBox();
    public final HBox rootPane = new HBox();
    public final JFrame main = this;
    public ComponentStorage componentStorage;// siirä viewmap aloitus formiin sitten kuin se on tehty


    private BorderPane scriptInfoLabel = null;
    private HBox scriptInfoPane = null;
    private BorderPane keySectionPane = null;
    private BorderPane actionSectionPane = null;


    public AHKInterface() {
        componentStorage = new ComponentStorage(main);
        componentStorage.setAhkinterface(this);
        constructAHK();
    }

    private void constructAHK() {
        this.add(ahkinterfaceView);
        this.setVisible(true);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        initComponents(ahkinterfaceView);
        componentStorage.findAHKScripts();
    }

    private void initComponents(JFXPanel jfxPanel) {
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene() {
        Scene scene = new Scene(menuPaneAkaRealRootPane, 1000, 600);
        menuPaneAkaRealRootPane.getChildren().add(componentStorage.menuSetup.createMenuBar());
        menuPaneAkaRealRootPane.getChildren().add(rootPane);
        menuPaneAkaRealRootPane.setVgrow(rootPane,Priority.ALWAYS);
        createScriptPane();
        createInfoPane();
        rootPane.getStylesheets().add(this.getClass().getResource("Css/main_btns.css").toExternalForm());
        return (scene);
    }
    private void createInfoPane(){
        VBox rootInfoPane = new VBox();

        scriptInfoLabel = createLabelpane(rootInfoPane);
        scriptInfoPane = createScriptInfoPane(rootInfoPane);
        HBox bottomButtonPane = createBottomButtonPane(rootInfoPane);

        rootInfoPane.getChildren().addAll(scriptInfoLabel,scriptInfoPane,bottomButtonPane);
        rootPane.setHgrow(rootInfoPane,Priority.ALWAYS);
        rootPane.getChildren().addAll(rootInfoPane);
    }
    private HBox createBottomButtonPane(VBox rootInfoPane){
        HBox bottomButtonPane = new HBox();
        Button changeKey = new Button("Change key");
        changeKey.setMaxSize(Double.MAX_VALUE,50);
        bottomButtonPane.setHgrow(changeKey,Priority.ALWAYS);
        Button changeAction = new Button("Change action");
        changeAction.setMaxSize(Double.MAX_VALUE,50);
        bottomButtonPane.setHgrow(changeAction,Priority.ALWAYS);
        Button editTask = new Button("Edit Scheduled Task");
        editTask.setMaxSize(Double.MAX_VALUE,50);
        bottomButtonPane.setHgrow(editTask,Priority.ALWAYS);
        Button run = new Button("Run");
        run.setMaxSize(Double.MAX_VALUE,50);
        bottomButtonPane.setHgrow(run,Priority.ALWAYS);

        bottomButtonPane.getStylesheets().add(this.getClass().getResource("Css/main_btns.css").toExternalForm());
        bottomButtonPane.getChildren().addAll(changeKey,changeAction,editTask,run);

        bottomButtonPane.setMaxSize(Double.MAX_VALUE,50);
        rootInfoPane.setVgrow(bottomButtonPane,Priority.ALWAYS);
        return bottomButtonPane;
    }
    private HBox createScriptInfoPane(VBox rootInfoPane){
        HBox scriptInfoPane = new HBox();
        keySectionPane = new BorderPane();
        keySectionPane.setStyle("-fx-border-color: #A9A9A9");
        actionSectionPane = new BorderPane();
        actionSectionPane.setStyle("-fx-border-color: #A9A9A9");
        Label firstlabel = new Label("Key");
        Label secondLabel = new Label("Action");
        firstlabel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        firstlabel.setAlignment(Pos.CENTER);
        secondLabel.setAlignment(Pos.CENTER);
        secondLabel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        keySectionPane.setCenter(firstlabel);
        actionSectionPane.setCenter(secondLabel);
        keySectionPane.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        actionSectionPane.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        scriptInfoPane.setHgrow(keySectionPane,Priority.ALWAYS);
        scriptInfoPane.setHgrow(actionSectionPane,Priority.ALWAYS);
        scriptInfoPane.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        scriptInfoPane.getChildren().addAll(keySectionPane,actionSectionPane);
        rootInfoPane.setVgrow(scriptInfoPane,Priority.ALWAYS);

        return scriptInfoPane;
    }
    private BorderPane createLabelpane(VBox rootInfoPane){
        BorderPane labelPane = new BorderPane();

        Label scriptNameLabel = new Label();
        scriptNameLabel.getStylesheets().add(this.getClass().getResource("Css/label.css").toExternalForm());
        scriptNameLabel.setAlignment(Pos.CENTER);
        labelPane.setCenter(scriptNameLabel);
        rootInfoPane.setVgrow(labelPane,Priority.ALWAYS);
        rootInfoPane.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        return labelPane;
    }
    private void createScriptPane(){
        VBox ScriptPane = new VBox(10);

        createMinusAndPlusButtons(ScriptPane);
        createScriptLabels(ScriptPane);
        createFindScriptsBottomButton(ScriptPane);

        ScriptPane.setStyle("-fx-border-color: #A9A9A9");
        rootPane.setHgrow(ScriptPane,Priority.ALWAYS);
        rootPane.getChildren().add(ScriptPane);
    }
    private void createFindScriptsBottomButton(VBox scriptPane){
        BorderPane bottomButonPane = new BorderPane();
        Button searchScripts = new Button("Search Scripts");
        searchScripts.setMaxWidth(800/2.8);
        searchScripts.setAlignment(Pos.BOTTOM_CENTER);
        bottomButonPane.setBottom(searchScripts);
        scriptPane.getChildren().add(bottomButonPane);
    }
    private void createScriptLabels(VBox scriptPane){
        VBox labelPane = new VBox();
        Label sectionTitleYourScripts = new Label("Your Scripts");
        sectionTitleYourScripts.setStyle("-fx-font-family: Lucida Sans Unicode;" +
                "-fx-font-size: 20px");
        labelPane.getChildren().add(sectionTitleYourScripts);

        final File yourScriptOriginal = new File("YourScripts/");
        File[] files = null;
        if(yourScriptOriginal != null && yourScriptOriginal.listFiles() != null) files = yourScriptOriginal.listFiles();
        if(files != null) for(File file : files){
            if(file.getAbsolutePath().endsWith(".ahk")){
                String scriptName = file.getName().replaceFirst("[.][^.]+$", "");;
                Label scriptLabel = new Label(scriptName);
                scriptLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        BufferedReader reader = null;
                        ArrayList<String> actionsinthisshit = new ArrayList<>();
                        ArrayList<String> keysthatareinthisfile = new ArrayList<>();
                        try{
                            reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                            ArrayList<String> insidesOfTheFile = new ArrayList<>();
                            String sCurrentline;
                            while((sCurrentline = reader.readLine()) != null){
                                insidesOfTheFile.add(sCurrentline);
                            }
                            Supplier<Stream<String>> streamSupplier = () -> insidesOfTheFile.stream();
                            ActionsData actionsData = new ActionsData();
                            ArrayList<Actions> listOfAllTheActions = actionsData.getActions();
                            for(Actions action : listOfAllTheActions){
                                HashMap<String,String[]> somemap = actionsData.readAllActionsToHashMap();
                                String[] array = somemap.get(action.getAction());
                                for(String jotain : array){
                                    streamSupplier.get().filter(oneline -> oneline.contains(jotain)).forEach(actionsinthisshit::add);
                                }
                                /*
                                "You have to re-think your identifition algorythm for the script reading,
                                 because you will get same lines in couple of different places and therefor you cant add by identifying one line of the code."
                                 */
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally{
                            try { reader.close(); } catch (IOException e) {}
                        }
                    }
                });
                labelPane.getChildren().add(scriptLabel);
            }
        }
        labelPane.setMaxSize(800/2.8,350);
        labelPane.setStyle("-fx-font-family: Lucida Sans Unicode;" +
                "-fx-font-size: 15px;" +
                "-fx-text-aligment: center");
        scriptPane.setVgrow(labelPane,Priority.ALWAYS);
        scriptPane.getChildren().add(labelPane);
    }
    private void createMinusAndPlusButtons(VBox scriptPane){
        HBox minusplusPane = new HBox();
        Button plus = new Button("+");
        plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.nameofthescript = JOptionPane.showInputDialog(main,"Name your Script");
                componentStorage.hideSelectedAndShowSelected(ahkinterfaceView,componentStorage.viewMap.get("keyselection"));
            }
        });
        plus.setTooltip(componentStorage.createTooltip("Create a new Script"));
        plus.setMaxSize(Double.MAX_VALUE,25);
        Button minus = new Button("-");
        minus.setTooltip(componentStorage.createTooltip("Delete script"));
        minus.setMaxSize(Double.MAX_VALUE,25);
        minusplusPane.setHgrow(plus,Priority.ALWAYS);
        minusplusPane.setHgrow(minus,Priority.ALWAYS);
        minusplusPane.getChildren().addAll(plus,minus);
        scriptPane.getChildren().add(minusplusPane);
    }
    public static void main(String[] args) {
        new AHKInterface();
    }
}
