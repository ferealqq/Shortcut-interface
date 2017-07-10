package ahk.pkginterface.commentFrames;

import ahk.pkginterface.AHKInterface;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javax.swing.*;

public class DetectDialog extends JFrame{
    public final JFXPanel jfxPanel = new JFXPanel();
    private final BorderPane rootPane = new BorderPane();

    private final Label dialogLabel = new Label("Press the keys you'd like to save for your script!");
    private final TextField textField = new TextField();
    private EventHandler<KeyEvent> textFieldInput;

    private AHKInterface mainFrame;

    public DetectDialog(AHKInterface mainForm){
    mainFrame = mainForm;
    this.setSize(400,200);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.add(jfxPanel);
    initComponents(jfxPanel);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene(  ) {
        Scene scene = new Scene(rootPane,800,500);
        createComponents();
        createListeners();
        return (scene);
    }
    private void createListeners() {
        textFieldInput = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode());
                System.out.println(event.getCharacter());
            }
        };
        textField.setOnKeyReleased(textFieldInput);
    }
    private void createComponents() {
        BorderPane.setAlignment(dialogLabel, Pos.TOP_CENTER);
        BorderPane.setAlignment(textField, Pos.CENTER);
        rootPane.setCenter(dialogLabel);
        rootPane.setCenter(textField);
    }

    public static void main(String[] args) {
        DetectDialog k = new DetectDialog(new AHKInterface());
        k.setVisible(true);
    }
}
