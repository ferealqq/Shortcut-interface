package ahk.pkginterface;

import ahk.pkginterface.commentFrames.commentFrame;
import ahk.pkginterface.database.ProfilesData;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;


public class Register{
    public final JFXPanel registerView = new JFXPanel();
    public final VBox rootPane = new VBox();

    private ProfilesData profilesDb = new ProfilesData();
    public final ViewStorage viewStorage;
    private String commentMsg;
    private commentFrame comment = new commentFrame();

    public Register(ViewStorage viewArchive) {
        viewStorage = viewArchive;
        initComponents(registerView);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene(){
        Scene scene = new Scene(rootPane,800,500);
        //viewStorage.menuSetup.setRootPane(rootPane);
        createComponents();
        return (scene);
    }
    private void createComponents(){
        ArrayList<components> labelandfieldStorage = new ArrayList<>();
        Label lbUsername = new Label("Username");
        lbUsername.setWrapText(true);
        TextField tfUsername = new TextField();
        labelandfieldStorage.add(new components(tfUsername,lbUsername));
        Label lbEmail = new Label("Email");
        TextField tfEmail = new TextField();
        labelandfieldStorage.add(new components(tfEmail,lbEmail));
        Label lbPassword = new Label("Password");
        PasswordField tfPassword = new PasswordField();
        labelandfieldStorage.add(new components(tfPassword,lbPassword));
        Label lbPassword2 = new Label("Password again!");
        PasswordField tfPassword2 = new PasswordField();
        labelandfieldStorage.add(new components(tfPassword2,lbPassword2));
        createListeners(tfUsername,tfEmail,tfPassword,tfPassword2);
        for (int i = 0; i < labelandfieldStorage.size(); i++) {
            BorderPane.setAlignment(labelandfieldStorage.get(i).getLb(),Pos.CENTER);
            BorderPane.setAlignment(labelandfieldStorage.get(i).getTf(),Pos.CENTER);
            labelandfieldStorage.get(i).getLb().setFont(new Font("Cambria", 32));
            labelandfieldStorage.get(i).getTf().setFont(new Font("Cambria", 20));
            labelandfieldStorage.get(i).getTf().setPrefHeight(40);
            labelandfieldStorage.get(i).getTf().setMaxWidth(300);
            rootPane.getChildren().addAll(new BorderPane(labelandfieldStorage.get(i).getLb()), new BorderPane(labelandfieldStorage.get(i).getTf()));
        }
        }
        public JFXPanel giveView(){
            return registerView;
        }
        /*
        * a quick shadowclass to store components in. To make code cleaner and easier to undestand by others.s
         */
    class components{
        public Label lb;
        public TextField tf;
        public components(TextField tfield, Label lbel){
            lb = lbel;
            tf = tfield;
        }
        public Label getLb(){
            return lb;
        }
        public TextField getTf(){
            return tf;
        }
    }
    private void createListeners(TextField tfUsername,TextField tfEmail,PasswordField tfPassword, PasswordField tfPassword2){
         tfUsername.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            public void handle(javafx.scene.input.KeyEvent keyEvent) {
                if(!profilesDb.checkUsername(tfUsername.getText())){
                    Stage dialog = createMessage("Username taken! Try something new!");
                    dialog.show();
                    dialog.setY(tfUsername.getTranslateY()+tfUsername.getLayoutY()+tfUsername.gety);
                    dialog.setX(tfUsername.getTranslateX()+viewStorage.mainFrame.getAlignmentX());
                    tfUsername.setStyle("-fx-text-fil: red;");
                    //tfUsername.addMouseListener(setHover);
                }else{
                    //tfUsername.setForeground(Color.BLACK);
                    //tfUsername.removeMouseListener(setHover);
                }
            }
        });
    }
    public Stage createMessage(String message){
        final Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        BorderPane dialogPane = new BorderPane();
        dialogPane.setCenter(new Text(message));
        Scene dialogScene = new Scene(dialogPane);
        dialog.setScene(dialogScene);
        return dialog;
    }
    public static void main(String[] args) {
    }
}
