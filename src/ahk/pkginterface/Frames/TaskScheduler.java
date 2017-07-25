package ahk.pkginterface.Frames;

import ahk.pkginterface.AHKInterface;
import ahk.pkginterface.ViewManagement.ComponentStorage;
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
import java.util.Objects;

import static javax.script.ScriptEngine.FILENAME;

public class TaskScheduler {
    public final JFXPanel taskSchedulerView = new JFXPanel();
    private final ComponentStorage componentStorage;
    public final VBox rootPane = new VBox();

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

        createActions(btnPath,btnSave);
        bottomPane.getChildren().addAll(btnPublish,btnPath,btnSave);
        rootPane.setVgrow(bottomPane,Priority.ALWAYS);
        rootPane.getChildren().add(bottomPane);
    }
    private void createActions(Button btnPath,Button btnSave){
        btnPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File path = directoryChooser.showDialog(null);
                choosenPath = path.getAbsolutePath();
            }
        });
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nameofthescript = JOptionPane.showInputDialog(componentStorage.mainFrame,"Name your script");
                if(Objects.nonNull(choosenPath)){
                }else{
                    BufferedReader reader = null;
                    BufferedWriter writer = null;
                    try {
                        System.out.println("trying");
                        for (String choosenActionPath:componentStorage.choosenActionPath) {
                            System.out.println("in for");
                            reader = new BufferedReader(new FileReader(choosenActionPath));

                            File file = new File("Your scripts/"+nameofthescript+".ahk");
                            if (!file.exists()) {
                                System.out.println("asdk");
                                file.createNewFile();
                            }

                            writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                            String firstRow = componentStorage.pressedKeys.get(0).getKeysynonyminahk();
                            for (int i = 1; i < componentStorage.pressedKeys.size(); i++) {
                                firstRow = firstRow +" & "+ componentStorage.pressedKeys.get(i).getKeysynonyminahk();
                            }
                            System.out.println(firstRow);
                            writer.write(firstRow+"::");
                            String sCurrentLine;
                            while ((sCurrentLine = reader.readLine()) != null) {
                                writer.newLine();
                                writer.write(sCurrentLine);
                                System.out.println(sCurrentLine);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        close(writer);
                        close(reader);
                    }
                }
            }
        });
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
    private void createRadioButtons(){
        HBox radiobuttonPane = new HBox();
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton btnRunOnStart = new RadioButton("Run everytime computer starts");
        btnRunOnStart.setToggleGroup(toggleGroup);
        btnRunOnStart.setUserData("Run everytime computer starts");
        RadioButton btnAskOnStart = new RadioButton("Ask everytime computer starts");
        btnAskOnStart.setToggleGroup(toggleGroup);
        btnAskOnStart.setUserData("Ask everytime computer starts");
        RadioButton btnRunWhenClockIs = new RadioButton("Write the time when to run the script");
        btnRunWhenClockIs.setToggleGroup(toggleGroup);
        btnRunWhenClockIs.setUserData("Write the time when to run the script");
        TextField tfTime = new TextField("00:00");
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
        radiobuttonPane.getChildren().addAll(btnRunOnStart,btnAskOnStart,btnRunWhenClockIs,tfTime);
        rootPane.getChildren().add(radiobuttonPane);
    }

    public static void main(String[] args) {
        new AHKInterface();
    }
}
