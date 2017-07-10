package ahk.pkginterface;

import ahk.pkginterface.commentFrames.commentFrame;
import ahk.pkginterface.database.ProfilesData;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Register{
    public final JFXPanel registerView = new JFXPanel();
    public final VBox rootPane = new VBox();

    private ProfilesData profilesDb = new ProfilesData();
    public final ViewStorage viewStorage;
    private final Tooltip tooltip;
    private String commentMsg;
    private commentFrame comment = new commentFrame();

    public Register(ViewStorage viewArchive) {
        viewStorage = viewArchive;
        initComponents(registerView);
        tooltip = viewArchive.createTooltip("");
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene(){
        Scene scene = new Scene(rootPane,800,500);
        createComponents();
        return (scene);
    }
    private void createComponents(){
        String registerCss = this.getClass().getResource("Css/register_and_login_style.css").toExternalForm();
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
        for (int i = 0; i < labelandfieldStorage.size(); i++) {
            BorderPane.setAlignment(labelandfieldStorage.get(i).getLb(),Pos.CENTER);
            BorderPane.setAlignment(labelandfieldStorage.get(i).getTf(),Pos.CENTER);
            labelandfieldStorage.get(i).getLb().setFont(new Font("Cambria", 32));
            labelandfieldStorage.get(i).getTf().setFont(new Font("Cambria", 20));
            labelandfieldStorage.get(i).getTf().setPrefHeight(40);
            labelandfieldStorage.get(i).getTf().setMaxWidth(300);
            BorderPane labelPane = new BorderPane(labelandfieldStorage.get(i).getLb());
            BorderPane textfieldPane = new BorderPane(labelandfieldStorage.get(i).getTf());
            labelPane.getStylesheets().add(registerCss);
            textfieldPane.getStylesheets().add(registerCss);
            rootPane.getChildren().addAll(labelPane,textfieldPane);
        }
        Button btnRegister = new Button("Register");
        btnRegister.getStylesheets().add(this.getClass().getResource("Css/ahk_main_bottombtns_css.css").toExternalForm());
        BorderPane.setAlignment(btnRegister,Pos.CENTER);
        btnRegister.setMaxSize(150,50);
        BorderPane btnPane = new BorderPane(btnRegister);
        btnPane.setPadding(new Insets(35,35,35,35));
        rootPane.getChildren().addAll(btnPane);
        createListeners(tfUsername,tfEmail,tfPassword,tfPassword2,btnRegister);
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
    private void createListeners(TextField tfUsername,TextField tfEmail,PasswordField tfPassword, PasswordField tfPassword2,Button btnRegister){
         tfUsername.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            public void handle(javafx.scene.input.KeyEvent keyEvent) {
                if(!profilesDb.checkUsername(tfUsername.getText())){
                    tfUsername.setStyle("-fx-text-inner-color: red;");
                    tooltip.setText("Username taken! Try something new!");
                    tfUsername.setTooltip(tooltip);
                }else{
                    tfUsername.setStyle("-fx-text-inner-color: black;");
                    Tooltip.uninstall(tfUsername,tooltip);
                }
            }
        });
        tfEmail.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(!profilesDb.checkEmail(tfEmail.getText())){
                    tfEmail.setStyle("-fx-text-inner-color: red;");
                    tooltip.setText("Email taken! Try something new!");
                    tfEmail.setTooltip(tooltip);
                }else{
                    tfEmail.setStyle("-fx-text-inner-color: black;");
                    Tooltip.uninstall(tfEmail,tooltip);
                }
            }
        });
        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tfPassword.getText().equals(tfPassword2.getText())){
                    if(!profilesDb.createUser(tfUsername.getText(),tfEmail.getText(),tfPassword.getText())) {
                        JOptionPane.showMessageDialog(viewStorage.mainFrame,"Something went wrong!");
                    }else{
                        JOptionPane.showMessageDialog(viewStorage.mainFrame,"Successful!");
                        viewStorage.showBackwardsHideCurrent();
                        viewStorage.currentUserId = profilesDb.getProileIdByUsername(tfUsername.getText());
                    }   
                }else{
                    JOptionPane.showMessageDialog(viewStorage.mainFrame,"Passwords won't match!");
                }
            }
        });
    }
}
