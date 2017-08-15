package ahk.pkginterface;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.ActionsData;
import com.sun.org.apache.bcel.internal.classfile.LineNumber;
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

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class AHKInterface extends JFrame {
    public final JFXPanel ahkinterfaceView = new JFXPanel();
    public final VBox menuPaneAkaRealRootPane = new VBox();
    public final HBox rootPane = new HBox();
    public final JFrame main = this;
    public ComponentStorage componentStorage;// siirä viewmap aloitus formiin sitten kuin se on tehty

    public final HashMap<String,ArrayList<String>> keyAndAListOfActionsInCurrentScript = new HashMap<>();

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
        Button btRenameTheScript = new Button("Rename");
        btRenameTheScript.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(btRenameTheScript, Priority.ALWAYS);
        Button btChangeKey = new Button("Change key");
        btChangeKey.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(btChangeKey, Priority.ALWAYS);
        Button btChangeAction = new Button("Change action");
        btChangeAction.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(btChangeAction, Priority.ALWAYS);
        Button btEditTask = new Button("Edit Scheduled Task");
        btEditTask.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(btEditTask, Priority.ALWAYS);
        Button btRun = new Button("Run");
        btRun.setMaxSize(Double.MAX_VALUE, 50);
        bottomButtonPane.setHgrow(btRun, Priority.ALWAYS);

        createButtonActions(btRenameTheScript,btChangeKey,btChangeAction,btEditTask,btRun);

        bottomButtonPane.getStylesheets().add(this.getClass().getResource("Css/main_btns.css").toExternalForm());
        bottomButtonPane.getChildren().addAll(btRenameTheScript,btChangeKey, btChangeAction, btEditTask, btRun);

        bottomButtonPane.setMaxSize(Double.MAX_VALUE, 50);
        return bottomButtonPane;
    }
    private void createButtonActions(Button btRenameTheScript,Button btChangeKey, Button btChangeAction,Button btEditTask,Button btRun){
        btRenameTheScript.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newName = JOptionPane.showInputDialog(main,"Write new name of the script","Rename");
                if(!newName.isEmpty()){
                    Boolean bool = componentStorage.changeKeyInfo.currentScriptDisblayedFile.renameTo(new File(componentStorage.changeKeyInfo.currentScriptDisblayedFile.getParent()+"\\"+newName+".ahk"));
                    if(bool) {
                        scriptNameLabel.setText(newName);
                        componentStorage.changeKeyInfo.currentScriptDisblayedLabel.setText(newName);
                    }
                }
            }
        });
        btChangeKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.changeKey.disableRightKeys();
                componentStorage.hideSelectedAndShowSelected((JFXPanel)main.getContentPane().getComponent(main.getContentPane().getComponentCount()-1),componentStorage.viewMap.get("changekey"));
            }
        });
        btChangeAction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.hideSelectedAndShowSelected((JFXPanel)main.getContentPane().getComponent(main.getContentPane().getComponentCount()-1),componentStorage.viewMap.get("changeaction"));
            }
        });
        btEditTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        btRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("run.bat"));
                    writer.write("start " + componentStorage.changeKeyInfo.currentScriptDisblayedFile.getAbsolutePath());
                    writer.newLine();
                    writer.write("exit");
                    Process batRunner = Runtime.getRuntime().exec("cmd /c start run.bat");
                    writer.close();
                } catch (IOException e) {
                }
            }
        });
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
                        oldColorReplacement(labelPane.getChildren().iterator());
                        if (!scriptLabel.getStyle().equals("-fx-background-color: #A9A9A9;")) {
                            componentStorage.changeKeyInfo.currentScriptDisblayedFile = file;
                            componentStorage.changeKeyInfo.currentScriptDisblayedLabel = scriptLabel;
                            scriptLabel.setStyle("-fx-background-color: #A9A9A9;");
                            scriptNameLabel.setText(scriptName);
                            LineNumberReader reader = null;
                            try {
                                reader = new LineNumberReader(new FileReader(file.getAbsolutePath()));
                                HashMap<String,Integer> keysIndex = new HashMap<>();
                                ArrayList<String> insidesOfTheFile = new ArrayList<>();
                                String sCurrentline;
                                while ((sCurrentline = reader.readLine()) != null) {
                                    insidesOfTheFile.add(sCurrentline);
                                    if(sCurrentline.endsWith("::")) keysIndex.put(sCurrentline.replace(":","").replace(" ",""),reader.getLineNumber());
                                }
                                deleteOldScriptInfo();
                                componentStorage.changeKeyInfo.keysIndexInScript.put(scriptName,keysIndex);
                                createCurrentScriptInfo(insidesOfTheFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                }
                            }
                        }else{
                            componentStorage.changeKeyInfo.currentScriptDisblayedFile = null;
                            componentStorage.changeKeyInfo.currentScriptDisblayedLabel = null;
                            deleteOldScriptInfo();
                            scriptLabel.setStyle("-fx-background-color: transparent");
                            scriptNameLabel.setText("");
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
    private void deleteOldScriptInfo(){
        Object[] listofkeys = keyAndAListOfActionsInCurrentScript.keySet().toArray();
        for(Object key :listofkeys){
            keyAndAListOfActionsInCurrentScript.remove(key.toString());
        }
        Iterator<Node> keySectionPaneIterator = keySectionPane.getChildren().iterator();
        while(keySectionPaneIterator.hasNext()){
            Node node = keySectionPaneIterator.next();
            if(node.getClass().equals(VBox.class)){
                VBox vBox = (VBox)node;
                Iterator<Node> iterator = vBox.getChildren().iterator();
                while(iterator.hasNext()){
                    if(iterator.next().getClass().equals(Label.class)) iterator.remove();
                }
            }
        }
        Iterator<Node> actionSectionPaneIterator = actionSectionPane.getChildren().iterator();
        while(actionSectionPaneIterator.hasNext()){
            Node node =actionSectionPaneIterator.next();
            if(node.getClass().equals(VBox.class)){
                VBox vBox = (VBox)node;
                Iterator<Node> iterator = vBox.getChildren().iterator();
                while(iterator.hasNext()){
                    if(iterator.next().getClass().equals(Label.class)) iterator.remove();
                }
            }
        }
    }

    private void createCurrentScriptInfo(ArrayList<String> insidesofTheFile){
        VBox actionsPane = new VBox(1);
        VBox keyPane = new VBox(1);
        analyzeTheScript(insidesofTheFile);

        Object[] keysFromMap = keyAndAListOfActionsInCurrentScript.keySet().toArray();
        for(Object keyFromMap : keysFromMap){
            String keyFromMapToString = keyFromMap.toString();
            List<String> listOfActions = keyAndAListOfActionsInCurrentScript.get(keyFromMapToString).stream().distinct().collect(Collectors.toList());
            Label currentActionlabel = new Label(listOfActions.toString());
            Label currentKeyLabel = new Label(keyFromMapToString.replace(":"," "));
            currentKeyLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Iterator<Node> iterator = keyPane.getChildren().iterator();
                    oldColorReplacement(iterator);
                    if(!currentKeyLabel.getStyle().equals("-fx-background-color: #A9A9A9;")){
                        currentKeyLabel.setStyle("-fx-background-color: #A9A9A9;");
                        componentStorage.changeKeyInfo.currentKeyDisblayedLabel = currentKeyLabel;
                    }else{
                        componentStorage.changeKeyInfo. currentKeyDisblayedLabel = null;
                        currentKeyLabel.setStyle("-fx-background-color: transparent");
                    }
                }
            });
            currentActionlabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Iterator<Node> iterator = actionsPane.getChildren().iterator();
                    oldColorReplacement(iterator);
                    if(!currentActionlabel.getStyle().equals("-fx-background-color: #A9A9A9;")){
                        currentActionlabel.setStyle("-fx-background-color: #A9A9A9;");
                        componentStorage.changeKeyInfo.currentActionDisbalayedLabel = currentActionlabel;
                    }else{
                        componentStorage.changeKeyInfo.currentActionDisbalayedLabel = null;
                        currentActionlabel.setStyle("-fx-background-color: transparent");
                    }
                }
            });
            actionsPane.getChildren().add(currentActionlabel);
            keyPane.getChildren().add(currentKeyLabel);
        }
        keyPane.setAlignment(Pos.CENTER);
        actionsPane.setAlignment(Pos.CENTER);
        keySectionPane.getChildren().add(keyPane);
        actionSectionPane.getChildren().add(actionsPane);
    }
    private  void oldColorReplacement(Iterator<Node> iterator){
        while(iterator.hasNext()){
            Node iteratorNextNode = iterator.next();
            Label oldLabelWithWrongColor = null;
            if(iteratorNextNode.getClass().equals(Label.class))  oldLabelWithWrongColor = (Label)iteratorNextNode;
            if(oldLabelWithWrongColor.getStyle().equals("-fx-background-color: #A9A9A9;")) oldLabelWithWrongColor.setStyle("-fx-background-color: transparent");
        }
    }
    public void analyzeTheScript(ArrayList<String> insidesOfTheFile){
        Iterator<String> insidesOfTheFileIterator = insidesOfTheFile.iterator();
        while(insidesOfTheFileIterator.hasNext()){
            String sCurenntString = insidesOfTheFileIterator.next();
            if(sCurenntString.endsWith("::")){
                if(insidesOfTheFileIterator.hasNext()) {
                    insidesOfTheFileIterator.next();
                    returnsTheActionsInOneKey(insidesOfTheFileIterator,sCurenntString);
                }else{
                    break;
                }
            }
        }
    }
    public HashMap<String,ArrayList<String>> returnsTheActionsInOneKey(Iterator<String> iterator,String currentKey){
        ArrayList<String> actionsFound = new ArrayList<>();
        ActionsData actionsData = new ActionsData();
        HashMap<String, String[]> mapOfActionsAndTheirCode = actionsData.readAllActionsToHashMap();
        Object[] keysAsObject = mapOfActionsAndTheirCode.keySet().toArray();
        while(iterator.hasNext()){
            String currentString = iterator.next();
            if(currentString.endsWith("::")) {
                returnsTheActionsInOneKey(iterator,currentString);
                break;
            }
            for(Object key: keysAsObject){
                String keyToString = key.toString();
                String[] linesOfCode = mapOfActionsAndTheirCode.get(keyToString);
                for(String lineofCodeFromMap: linesOfCode){
                    if(currentString.equals(lineofCodeFromMap)){
                        actionsFound.add(keyToString);
                    }
                }
            }
        }
        keyAndAListOfActionsInCurrentScript.put(currentKey,actionsFound);
        return keyAndAListOfActionsInCurrentScript;
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
