package ahk.pkginterface;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Actions;
import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
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
    private VBox keySectionPane = null;
    private VBox actionSectionPane = null;
    private final Label scriptNameLabel = new Label();


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
        menuPaneAkaRealRootPane.setVgrow(rootPane, Priority.ALWAYS);
        createScriptPane();
        createInfoPane();
        rootPane.getStylesheets().add(this.getClass().getResource("Css/main_btns.css").toExternalForm());
        return (scene);
    }

    private void createInfoPane() {
        BorderPane rootInfoPane = new BorderPane();

        scriptInfoLabel = createLabelpane();
        scriptInfoPane = createScriptInfoPane();
        HBox bottomButtonPane = createBottomButtonPane();

        rootInfoPane.setTop(scriptInfoLabel);
        rootInfoPane.setCenter(scriptInfoPane);
        rootInfoPane.setBottom(bottomButtonPane);
        rootPane.setHgrow(rootInfoPane, Priority.ALWAYS);
        rootPane.getChildren().addAll(rootInfoPane);
    }

    private HBox createBottomButtonPane() {
        HBox bottomButtonPane = new HBox();
        Button changeKey = new Button("Change key");
        changeKey.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(changeKey, Priority.ALWAYS);
        Button changeAction = new Button("Change action");
        changeAction.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(changeAction, Priority.ALWAYS);
        Button editTask = new Button("Edit Scheduled Task");
        editTask.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(editTask, Priority.ALWAYS);
        Button run = new Button("Run");
        run.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(run, Priority.ALWAYS);

        bottomButtonPane.getStylesheets().add(this.getClass().getResource("Css/main_btns.css").toExternalForm());
        bottomButtonPane.getChildren().addAll(changeKey, changeAction, editTask, run);

        bottomButtonPane.setMaxSize(Double.MAX_VALUE, 50);
        return bottomButtonPane;
    }

    private HBox createScriptInfoPane() {
        HBox scriptInfoPane = new HBox();
        keySectionPane = new VBox();
        ScrollPane keySectionScrollPane = new ScrollPane();
        keySectionScrollPane.setStyle("-fx-border-color: #A9A9A9");
        actionSectionPane = new VBox();
        ScrollPane actionSectionScrollPane = new ScrollPane();
        actionSectionScrollPane.setStyle("-fx-border-color: #A9A9A9");
        Label firstlabel = new Label("Key");
        Label secondLabel = new Label("Action");
        firstlabel.setStyle("-fx-font-family: Lucida Sans Unicode;" +
                "-fx-font-size: 20px;" +
                "-fx-font-color: #A9A9A9");
        secondLabel.setStyle("-fx-font-family: Lucida Sans Unicode;" +
                "-fx-font-size: 20px;" +
                "-fx-font-color: #A9A9A9");
        firstlabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        firstlabel.setAlignment(Pos.CENTER);
        secondLabel.setAlignment(Pos.CENTER);
        secondLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        keySectionPane.getChildren().add(firstlabel);
        actionSectionPane.getChildren().add(secondLabel);
        keySectionPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        keySectionPane.setAlignment(Pos.CENTER);
        keySectionScrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        actionSectionPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        actionSectionPane.setAlignment(Pos.CENTER);
        actionSectionScrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scriptInfoPane.setHgrow(keySectionScrollPane, Priority.ALWAYS);
        scriptInfoPane.setHgrow(actionSectionScrollPane, Priority.ALWAYS);
        scriptInfoPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        keySectionScrollPane.setContent(keySectionPane);
        actionSectionScrollPane.setContent(actionSectionPane);
        keySectionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        keySectionPane.minWidthProperty().bind(Bindings.createDoubleBinding(() -> keySectionScrollPane.getViewportBounds().getWidth(), keySectionScrollPane.viewportBoundsProperty()));
        actionSectionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        actionSectionPane.minWidthProperty().bind(Bindings.createDoubleBinding(() -> actionSectionScrollPane.getViewportBounds().getWidth(), actionSectionScrollPane.viewportBoundsProperty()));
        scriptInfoPane.getChildren().addAll(keySectionScrollPane,actionSectionScrollPane);
        scriptInfoPane.setAlignment(Pos.CENTER);
        return scriptInfoPane;
    }

    private BorderPane createLabelpane() {
        BorderPane labelPane = new BorderPane();

        scriptNameLabel.getStylesheets().add(this.getClass().getResource("Css/label.css").toExternalForm());
        scriptNameLabel.setAlignment(Pos.CENTER);
        labelPane.setCenter(scriptNameLabel);

        return labelPane;
    }

    private void createScriptPane() {
        VBox ScriptPane = new VBox(10);

        createMinusAndPlusButtons(ScriptPane);
        createScriptLabels(ScriptPane);
        createFindScriptsBottomButton(ScriptPane);

        ScriptPane.setStyle("-fx-border-color: #A9A9A9");
        rootPane.setHgrow(ScriptPane, Priority.ALWAYS);
        rootPane.getChildren().add(ScriptPane);
    }

    private void createFindScriptsBottomButton(VBox scriptPane) {
        BorderPane bottomButonPane = new BorderPane();
        Button searchScripts = new Button("Search Scripts");
        searchScripts.setMaxWidth(800 / 2.8);
        searchScripts.setAlignment(Pos.BOTTOM_CENTER);
        bottomButonPane.setBottom(searchScripts);
        scriptPane.getChildren().add(bottomButonPane);
    }

    private void createScriptLabels(VBox scriptPane) {
        VBox labelPane = new VBox();
        Label sectionTitleYourScripts = new Label("Your Scripts");
        sectionTitleYourScripts.setStyle("-fx-font-family: Lucida Sans Unicode;" +
                "-fx-font-size: 20px");
        labelPane.getChildren().add(sectionTitleYourScripts);

        final File yourScriptOriginal = new File("YourScripts/");
        File[] files = null;
        if (yourScriptOriginal != null && yourScriptOriginal.listFiles() != null)
            files = yourScriptOriginal.listFiles();
        if (files != null) for (File file : files) {
            if (file.getAbsolutePath().endsWith(".ahk")) {
                String scriptName = file.getName().replaceFirst("[.][^.]+$", "");
                Label scriptLabel = new Label(scriptName);
                scriptLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        scriptLabel.setStyle("-fx-background-color: #A9A9A9;");
                        scriptNameLabel.setText(scriptName);
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                            ArrayList<String> insidesOfTheFile = new ArrayList<>();
                            String sCurrentline;
                            while ((sCurrentline = reader.readLine()) != null) {
                                insidesOfTheFile.add(sCurrentline);
                            }
                            Supplier<Stream<String>> insidesOfTheScriptInASupplier = () -> insidesOfTheFile.stream();
                            createCurrentScriptInfo(insidesOfTheScriptInASupplier);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                });
                labelPane.getChildren().add(scriptLabel);
            }
        }
        labelPane.setMaxSize(800 / 2.8, 350);
        labelPane.setStyle("-fx-font-family: Lucida Sans Unicode;" +
                "-fx-font-size: 15px");
        scriptPane.setVgrow(labelPane, Priority.ALWAYS);
        scriptPane.getChildren().add(labelPane);
    }

    private void createCurrentScriptInfo(Supplier<Stream<String>> insidesOfTheScriptInASupplier){
        ArrayList<String> ActionCodeInScript = new ArrayList<>();
        ArrayList<String> KeysInScript = new ArrayList<>();
        VBox actionsPane = new VBox(1);

        ActionsData actionsData = new ActionsData();
        HashMap<String, String[]> mapOfActionsAndTheirCode = actionsData.readAllActionsToHashMap();
        Object[] keysAsObject = mapOfActionsAndTheirCode.keySet().toArray();
        for(Object actionname : keysAsObject){
            String[] linesOfCode = mapOfActionsAndTheirCode.get(actionname.toString());
            for(String somethingfunny : linesOfCode){
                insidesOfTheScriptInASupplier.get().filter(oneline -> oneline.equals(somethingfunny)).forEach(ActionCodeInScript::add);
            }
            for(String containedCode : ActionCodeInScript){
                String[] oneActionCode = mapOfActionsAndTheirCode.get(actionname.toString());
                for(String OneLineOfCode : oneActionCode){
                    if(OneLineOfCode.equals(containedCode)) {
                        for(Node children : actionsPane.getChildren()){
                            Label labelInBox = (Label)children;
                            System.out.println(labelInBox.getText());
                            if(labelInBox.equals(actionname.toString())) System.out.println("bad meme");
                        }
                        Label test = new Label(actionname.toString());
                        actionsPane.getChildren().add(test);
                    }
                }
            }
        }
        actionsPane.setAlignment(Pos.CENTER);
        actionSectionPane.getChildren().add(actionsPane);
    }

    private void createMinusAndPlusButtons(VBox scriptPane) {
        HBox minusplusPane = new HBox();
        Button plus = new Button("+");
        plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.nameofthescript = JOptionPane.showInputDialog(main, "Name your Script");
                componentStorage.hideSelectedAndShowSelected(ahkinterfaceView, componentStorage.viewMap.get("keyselection"));
            }
        });
        plus.setTooltip(componentStorage.createTooltip("Create a new Script"));
        plus.setMaxSize(Double.MAX_VALUE, 25);
        Button minus = new Button("-");
        minus.setTooltip(componentStorage.createTooltip("Delete script"));
        minus.setMaxSize(Double.MAX_VALUE, 25);
        minusplusPane.setHgrow(plus, Priority.ALWAYS);
        minusplusPane.setHgrow(minus, Priority.ALWAYS);
        minusplusPane.getChildren().addAll(plus, minus);
        scriptPane.getChildren().add(minusplusPane);
    }

    public static void main(String[] args) {
        new AHKInterface();
    }
}
