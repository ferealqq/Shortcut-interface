package ahk.pkginterface.Frames;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.ProfilesData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import sun.java2d.cmm.Profile;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskScheduler {
    public final JFXPanel taskSchedulerView = new JFXPanel();
    private final ComponentStorage componentStorage;
    public final VBox rootPane = new VBox();

    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final TextField tfTime = new TextField("00:00");
    private final RadioButton btnRunNow = new RadioButton("Run Now!");
    private EventHandler<ActionEvent> btnSaveActionEvent;
    private EventHandler<ActionEvent> btnPathActionEvent;
    public String choosenPath;

    public TaskScheduler(ComponentStorage componentArchive) {
        componentStorage = componentArchive;
        initComponents();
    }
    /*
    setting components to taskscheduler view
     */
    private void initComponents() {
        Scene scene = createScene();
        this.taskSchedulerView.setScene(scene);
    }
    /*
    Creating a scene creating the components for the scene. Adding them to the scene. Returning the scene, completed.
     */
    private Scene createScene() {
        Scene scene = new Scene(rootPane);
        componentStorage.createStepBar(rootPane);
        componentStorage.highLightCurrentStep(3);
        createRadioButtons();
        createBottomPane();
        return scene;
    }

    private void createBottomPane() {
        HBox bottomPane = new HBox();

        Button btnPublish = new Button("Publish your script");
        btnPublish.setDisable(true);
        bottomPane.setHgrow(btnPublish, Priority.ALWAYS);
        btnPublish.setMaxWidth(Double.MAX_VALUE);
        btnPublish.setMaxHeight(Double.MAX_VALUE);

        Button btnPath = new Button("Choose the path where you want to save this script");
        bottomPane.setHgrow(btnPath, Priority.ALWAYS);
        btnPath.setMaxWidth(Double.MAX_VALUE);
        btnPath.setMaxHeight(Double.MAX_VALUE);

        Button btnSave = new Button("Save");
        bottomPane.setHgrow(btnSave, Priority.ALWAYS);
        btnSave.setMaxWidth(Double.MAX_VALUE);
        btnSave.setMaxHeight(Double.MAX_VALUE);

        createActions();

        btnSave.setOnAction(btnSaveActionEvent);
        btnPath.setOnAction(btnPathActionEvent);

        bottomPane.getChildren().addAll(btnPublish, btnPath, btnSave);
        rootPane.setVgrow(bottomPane, Priority.ALWAYS);
        rootPane.getChildren().add(bottomPane);
    }
    // this is to be commited
    private void createRadioButtons() {
        HBox FirstRadiobuttonPane = new HBox(40);
        RadioButton btnRunOnStart = new RadioButton("Run everytime computer starts");
        btnRunOnStart.setToggleGroup(toggleGroup);
        btnRunOnStart.setUserData("Run everytime computer starts");
        RadioButton btnAskOnStart = new RadioButton("Ask everytime computer starts");
        btnAskOnStart.setToggleGroup(toggleGroup);
        btnAskOnStart.setUserData("Ask everytime computer starts");
        HBox SecondRadioButtonPane = new HBox(40);
        RadioButton btnRunWhenClockIs = new RadioButton("Write the time when to run the script");
        btnRunWhenClockIs.setToggleGroup(toggleGroup);
        btnRunWhenClockIs.setUserData("Write the time when to run the script");
        tfTime.setDisable(true);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (toggleGroup.getSelectedToggle() != null) {
                    if (toggleGroup.getSelectedToggle().getUserData().toString().equals("Write the time when to run the script")) {
                        tfTime.setDisable(false);
                    } else {
                        tfTime.setDisable(true);
                    }
                }

            }
        });
        FirstRadiobuttonPane.getChildren().addAll(btnRunOnStart, btnAskOnStart);
        SecondRadioButtonPane.getChildren().addAll(btnRunWhenClockIs, tfTime, btnRunNow);
        rootPane.getChildren().addAll(FirstRadiobuttonPane, SecondRadioButtonPane);
    }

    private void createActions() {
        btnPathActionEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File path = directoryChooser.showDialog(null);
                choosenPath = path.getAbsolutePath();
                if(componentStorage.currentUserId != 0) {
                    new ProfilesData().insertScriptPaths(choosenPath,componentStorage.currentUserId);
                }else{
                    System.out.println(componentStorage.currentUserId);
                }
            }
        };
        btnSaveActionEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                    * ChoosenPath is the path which the client has choosen.
                    * ChoosenActionPath is the path to the file where you would find the action.
                 */
                if (Objects.isNull(choosenPath)) {
                    choosenPath = "YourScripts/" + componentStorage.nameofthescript + ".ahk";
                }
                File newScript = new File(choosenPath);
                if (checkForDoubleKey(newScript.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(componentStorage.mainFrame, "Key already has an action. Try another key!");
                    componentStorage.pressedKeys.removeAll(componentStorage.pressedKeys);
                    componentStorage.choosenActionName.removeAll(componentStorage.choosenActionName);
                    componentStorage.hideSelectedAndShowSelected((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1), componentStorage.viewMap.get("keyselection"));
                    resetColors();
                    return;
                }
                String absolutePathOfTheScript = writeScript(newScript);
                if (btnRunNow.isSelected()) {
                   componentStorage.runThisScript(newScript);
                }
                //if the file exists and the task all ready have been created you dont need to create a     new task
                createTask(componentStorage.nameofthescript, absolutePathOfTheScript);

                int integerofOptionAnwser = JOptionPane.showConfirmDialog(null,
                        "Saving sucessful! Would you like to add more actions to the same script", "choose one", JOptionPane.YES_NO_OPTION);
                boolean choosenAnwser = Boolean.valueOf(integerofOptionAnwser > 0 ? "false" : "true");
                if (choosenAnwser) {
                    componentStorage.pressedKeys.removeAll(componentStorage.pressedKeys);
                    componentStorage.choosenActionName.removeAll(componentStorage.choosenActionName);
                    componentStorage.hideSelectedAndShowSelected((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1), componentStorage.viewMap.get("keyselection"));
                    resetColors();
                }else{
                    componentStorage.shortcutinterface.createScriptLabel(newScript);
                    componentStorage.pressedKeys.removeAll(componentStorage.pressedKeys);
                    componentStorage.choosenActionName.removeAll(componentStorage.choosenActionName);
                    componentStorage.hideSelectedAndShowSelected((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1), componentStorage.viewMap.get("shortcutinterface"));
                    resetColors();
                }
            }
        };
    }

    private void resetColors() {
        for (Node node : componentStorage.keySelection.rootPane.getChildren()) {
            if (node.getClass().equals(VBox.class)) {
                VBox vBox = (VBox) node;
                for (Node nod : vBox.getChildren()) {
                    if (nod.getClass().equals(HBox.class)) {
                        HBox hbox = (HBox) nod;
                        for(Node btn : hbox.getChildren()){
                            if(btn.getStyle().equals("-fx-background-color: slateblue; -fx-text-fill: white;")) btn.setStyle(null);
                            if(btn.isDisabled()) btn.setDisable(false);
                        }
                    }
                }
            }
        }
        for (Node node : componentStorage.browseaction.labelPane.getChildren()) {
            if (node.getClass().equals(Label.class)) {
                Label label = (Label) node;
                if (label.getStyle().equals("-fx-background-color: #404040;")) {
                    label.setStyle("-fx-background-color: transparent;");
                }
            }
        }
    }

    private boolean checkForDoubleKey(String absolutePath) {
        BufferedReader reader = null;
        try {
            if (!new File(absolutePath).exists()) return false;
            reader = new BufferedReader(new FileReader(absolutePath));
            while (Objects.nonNull(reader.readLine())) {
                for (Key key : componentStorage.pressedKeys) {
                    String line = reader.readLine();
                    if (line != null && line.contains(key.keysynonyminahk)) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private String writeScript(File file) {
        BufferedWriter writer = null;
        try {
            if (file.exists()) {
                writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
                writeContentForScript(writer, file);
            } else {
                file.createNewFile();
                writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
                writeContentForScript(writer, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
            return file.getAbsolutePath();
        }
    }

    private void writeContentForScript(BufferedWriter writer, File file) {
        ArrayList<String> scriptContent = getScriptContent();
        if(!componentStorage.choosenActionName.stream().filter(actionName -> actionName.toLowerCase().contains("spotify")).collect(Collectors.toList()).isEmpty()){
            writeSporifyScript(writer,scriptContent);
            return;
        }
        try {
            writer.newLine();
            String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
            for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                firstRow = firstRow + " & " + componentStorage.pressedKeys.get(i).getKeysynonyminahk();
            }
            writer.write(firstRow + "::");
            writer.newLine();
            for (String actionLine : scriptContent) {
                writer.newLine();
                writer.write(actionLine);
            }
            writer.newLine();
            writer.newLine();
            writer.write("return");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }
    private void writeSporifyScript(BufferedWriter writer,ArrayList<String> scriptContent){
        ArrayList<String> spotifyScriptContent = getSpotifyScriptContent();
        try {
            writer.newLine();
            String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
            for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                firstRow = firstRow + " & " + componentStorage.pressedKeys.get(i).getKeysynonyminahk();
            }
            writer.write(firstRow + "::");
            for (int index = 0; index <=spotifyScriptContent.size();index++) {
                writer.newLine();
                if(index == 10 || index == 3){
                    for(String content : scriptContent){
                        writer.write(content);
                        writer.newLine();
                        System.out.println(content);
                    }
                }
                writer.write(spotifyScriptContent.get(index));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }
    private ArrayList<String> getSpotifyScriptContent(){
        return new ArrayList<String>(Arrays.asList(new ActionsData().getSpotifyActionCode()));
    }
    /*
    * getScriptContent is method that gathers choosen actioncode from database and returns the code as an arraylist of strings
     */
    private ArrayList<String> getScriptContent(){
        final ArrayList<String> listOfCode = new ArrayList<>();
        componentStorage.choosenActionName.stream().forEach(
                oneActionName ->{
                    for(String oneLine : new ActionsData().getActionCode(oneActionName)){
                        listOfCode.add(oneLine);
                    }
                }
        );
        return listOfCode;
    }
    private void createTask(String nameofthescript, String absolutePathOfTheScript) {
        try {
            File file = new File("TaskSchedule.bat");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter BatWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            if (Objects.nonNull(toggleGroup.getSelectedToggle()) && toggleGroup.getSelectedToggle().getUserData().toString().equals("Write the time when to run the script")) {
                BatWriter.write("SchTasks /Create /SC DAILY /TN \"" + nameofthescript + "\" /TR \"" + absolutePathOfTheScript + "\" /ST " + tfTime.getText());
            } else if(Objects.nonNull(toggleGroup.getSelectedToggle()) && toggleGroup.getSelectedToggle().getUserData().toString().equals("Run everytime computer starts")) {
                BatWriter.write("SchTasks /Create /SC ONSTART /TN \"" + nameofthescript + "\" /TR \"" + absolutePathOfTheScript + "\"");
            }
            BatWriter.newLine();
            BatWriter.write("exit");
            String replacedPath = file.getAbsolutePath().replace("\\", "\\\\");
            Process p = Runtime.getRuntime().exec("cmd /c start " + replacedPath);
            close(BatWriter);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(componentStorage.mainFrame, ex);
        }
    }

    public static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
        }
    }

    public JFXPanel giveView() {
        return taskSchedulerView;
    }


    public static void main(String[] args) throws IOException {

    }
}
