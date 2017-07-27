package ahk.pkginterface.Frames;

import ahk.pkginterface.AHKInterface;
import ahk.pkginterface.ViewManagement.ComponentStorage;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.util.Objects;

import static javax.script.ScriptEngine.FILENAME;

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
    public TaskScheduler(ComponentStorage componentArchive){
        componentStorage = componentArchive;
        initComponents();
    }
    private void initComponents() {
        Scene scene = createScene();
        this.taskSchedulerView.setScene(scene);
    }

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

        bottomPane.getChildren().addAll(btnPublish,btnPath,btnSave);
        rootPane.setVgrow(bottomPane,Priority.ALWAYS);
        rootPane.getChildren().add(bottomPane);
    }
    private void createRadioButtons(){
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
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (toggleGroup.getSelectedToggle() != null) {
                    if(toggleGroup.getSelectedToggle().getUserData().toString().equals("Write the time when to run the script")){
                        tfTime.setDisable(false);
                    }else{
                        tfTime.setDisable(true);
                    }
                }

            }
        });
        FirstRadiobuttonPane.getChildren().addAll(btnRunOnStart,btnAskOnStart);
        SecondRadioButtonPane.getChildren().addAll(btnRunWhenClockIs,tfTime,btnRunNow);
        rootPane.getChildren().addAll(FirstRadiobuttonPane,SecondRadioButtonPane);
    }
    private void createActions(){
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
                String nameofthescript = JOptionPane.showInputDialog(componentStorage.mainFrame,"Name your script");
                if(Objects.nonNull(choosenPath)){
                    BufferedReader reader = null;
                    BufferedWriter writer = null;
                    try {
                        for (String choosenActionPath:componentStorage.choosenActionPath) {
                            reader = new BufferedReader(new FileReader(choosenActionPath));

                            File file = new File(choosenPath+"/"+nameofthescript+".ahk");
                            System.out.println(file.getAbsolutePath());
                            file.createNewFile();
                            writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                            String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
                            for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                                firstRow = firstRow +" & "+ componentStorage.pressedKeys.get(i).getKeysynonyminahk();
                            }
                            writer.write(firstRow+"::");
                            String sCurrentLine;
                            while ((sCurrentLine = reader.readLine()) != null) {
                               writer.newLine();
                                writer.write(sCurrentLine);
                            }
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(componentStorage.mainFrame,e);
                    }finally {
                        close(writer);
                        close(reader);
                    }

                    if(btnRunNow.isSelected()){
                        try {
                            writer = new BufferedWriter(new FileWriter("run.bat"));
                            writer.write("start "+choosenPath+"/"+nameofthescript);
                            Process batRunner = Runtime.getRuntime().exec("cmd /c start run.bat");
                            System.out.println("run da script");
                        } catch (IOException e) {}
                    }
                    try {
                        File file = new File("TaskSchedule.bat");
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        BufferedWriter BatWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                        if(toggleGroup.getSelectedToggle().getUserData().toString().equals("Write the time when to run the script")){
                            BatWriter.write("SchTasks /Create /SC DAILY /TN \""+nameofthescript+"\" /TR \"E:AHK-Interface/YourScripts/"+nameofthescript+".ahk\" /ST "+tfTime.getText());
                        }else{
                            BatWriter.write("SchTasks /Create /SC ONSTART /TN \""+nameofthescript+"\" /TR \"E:AHK-Interface/YourScripts/"+nameofthescript+".ahk\" /ST "+tfTime.getText());
                        }
                        BatWriter.newLine();
                        BatWriter.write("exit");
                        String replacedPath = file.getAbsolutePath().replace("\\","\\\\");
                        Process p = Runtime.getRuntime().exec("cmd /c start "+replacedPath);
                        BatWriter.close();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(componentStorage.mainFrame,ex);
                    }

                }else{

                    BufferedReader reader = null;
                    BufferedWriter writer = null;
                    try {
                        for (String choosenActionPath:componentStorage.choosenActionPath) {
                            reader = new BufferedReader(new FileReader(choosenActionPath));

                            File file = new File("YourScripts/"+nameofthescript+".ahk");
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                            String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
                            for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                                firstRow = firstRow +" & "+ componentStorage.pressedKeys.get(i).getKeysynonyminahk();
                            }
                            writer.write(firstRow+"::");
                            String sCurrentLine;
                            while ((sCurrentLine = reader.readLine()) != null) {
                                writer.newLine();
                                writer.write(sCurrentLine);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        close(writer);
                        close(reader);
                    }
                    if(btnRunNow.isSelected()){
                        try {
                            writer = new BufferedWriter(new FileWriter("run.bat"));
                            writer.write("start YourScripts/"+nameofthescript+".ahk");
                            Process batRunner = Runtime.getRuntime().exec("cmd /c start run.bat");
                            System.out.println("run da script");
                            close(writer);
                        } catch (IOException e) {}
                    }
                    try {
                        File file = new File("TaskSchedule.bat");
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        BufferedWriter BatWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                        if(toggleGroup.getSelectedToggle().getUserData().toString().equals("Write the time when to run the script")){
                            BatWriter.write("SchTasks /Create /SC DAILY /TN \""+nameofthescript+"\" /TR \"E:AHK-Interface/YourScripts/"+nameofthescript+".ahk\" /ST "+tfTime.getText());
                        }else{
                            BatWriter.write("SchTasks /Create /SC ONSTART /TN \""+nameofthescript+"\" /TR \"E:AHK-Interface/YourScripts/"+nameofthescript+".ahk\" /ST "+tfTime.getText());
                        }
                        BatWriter.newLine();
                        BatWriter.write("exit");
                        String replacedPath = file.getAbsolutePath().replace("\\","\\\\");
                        Process p = Runtime.getRuntime().exec("cmd /c start "+replacedPath);
                        close(BatWriter);
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(componentStorage.mainFrame,ex);
                    }
                }
            }
        };
    }
    public static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch(IOException e) {
        }
    }
    public JFXPanel giveView(){
        return taskSchedulerView;
    }


    public static void main(String[] args) throws IOException {

    }
}
