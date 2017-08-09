package ahk.pkginterface.Frames;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Key;
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

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

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
                File file = new File(choosenPath);
                if (checkForDoubleKey(file.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(componentStorage.mainFrame, "Key already has an action. Try another key!");
                    componentStorage.pressedKeys.removeAll(componentStorage.pressedKeys);
                    componentStorage.choosenActionPath.removeAll(componentStorage.choosenActionPath);
                    componentStorage.hideSelectedAndShowSelected((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1), componentStorage.viewMap.get("keyselection"));
                    resetColors();
                    return;
                }
                String absolutePathOfTheScript = writeScript(file);
                if (btnRunNow.isSelected()) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("run.bat"));
                        writer.write("start " + absolutePathOfTheScript);
                        writer.newLine();
                        writer.write("exit");
                        Process batRunner = Runtime.getRuntime().exec("cmd /c start run.bat");
                        close(writer);
                    } catch (IOException e) {
                    }
                }
                //if the file exists and the task all ready have been created you dont need to create a     new task
                createTask(componentStorage.nameofthescript, absolutePathOfTheScript);

                int integerofOptionAnwser = JOptionPane.showConfirmDialog(null,
                        "Saving sucessful! Would you like to add more actions to the same script", "choose one", JOptionPane.YES_NO_OPTION);
                boolean choosenAnwser = Boolean.valueOf(integerofOptionAnwser > 0 ? "false" : "true");
                System.out.println(integerofOptionAnwser);
                if (choosenAnwser) {
                    componentStorage.pressedKeys.removeAll(componentStorage.pressedKeys);
                    componentStorage.choosenActionPath.removeAll(componentStorage.choosenActionPath);
                    componentStorage.hideSelectedAndShowSelected((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1), componentStorage.viewMap.get("keyselection"));
                    resetColors();
                }else{
                    System.exit(1);
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
        BufferedReader reader = null;
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
            close(reader);
            return file.getAbsolutePath();
        }
    }

    private void writeContentForScript(BufferedWriter writer, File file) {
        try {
            writer.newLine();
            String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
            for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                firstRow = firstRow + " & " + componentStorage.pressedKeys.get(i).getKeysynonyminahk();
            }
            writer.write(firstRow + "::");
            for (String actionLine : readChoosenAction()) {
                writer.newLine();
                writer.write(actionLine);
            }
            writer.newLine();
            writer.write("return");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }

    private ArrayList<String> readChoosenAction() {
        ArrayList<String> redLines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            for (String path : componentStorage.choosenActionPath) {
                reader = new BufferedReader(new FileReader(path));
                String sCurrentline;
                while ((sCurrentline = reader.readLine()) != null) {
                    redLines.add(sCurrentline);
                    System.out.println(sCurrentline);
                    for (int i = 0; i < sCurrentline.length(); i++) {
                        sCurrentline.replace(sCurrentline.charAt(0)+"","k");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return redLines;
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
