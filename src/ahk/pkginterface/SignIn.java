package ahk.pkginterface;

import ahk.pkginterface.database.ProfilesData;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.util.ArrayList;

public class SignIn {
    public final JFXPanel signInView = new JFXPanel();
    public final BorderPane mainPane = new BorderPane();
    public final VBox rootPane = new VBox();

    private ProfilesData profilesDb = new ProfilesData();
    public final ViewStorage viewStorage;

    public SignIn(ViewStorage viewArchive) {
        viewStorage = viewArchive;
        initComponents(signInView);
    }
    private void initComponents(JFXPanel jfxPanel){
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene(){
        Scene scene = new Scene(rootPane,800,500);
        mainPane.maxHeight(Double.MAX_VALUE);
        mainPane.maxWidth(Double.MAX_VALUE);
        mainPane.setPadding(new Insets(50,0,50,0));
        rootPane.setVgrow(mainPane, Priority.ALWAYS);
        rootPane.getChildren().add(mainPane);
        createComponents();
        return (scene);
    }
    private void createComponents(){
        String signInCss = this.getClass().getResource("Css/register_and_login_style.css").toExternalForm();
        ArrayList<components> labelandfieldStorage = new ArrayList<>();
        javafx.scene.control.Label lbUsername = new javafx.scene.control.Label("Username / Email");
        javafx.scene.control.TextField tfUsername = new javafx.scene.control.TextField();
        labelandfieldStorage.add(new components(tfUsername,lbUsername));
        javafx.scene.control.Label lbPassword = new javafx.scene.control.Label("Password");
        PasswordField tfPassword = new PasswordField();
        labelandfieldStorage.add(new components(tfPassword,lbPassword));
        for (int i = 0; i < labelandfieldStorage.size(); i++) {
            labelandfieldStorage.get(i).getLb().setFont(new javafx.scene.text.Font("Cambria", 32));
            labelandfieldStorage.get(i).getTf().setFont(new javafx.scene.text.Font("Cambria", 20));
            labelandfieldStorage.get(i).getTf().setPrefHeight(40);
            labelandfieldStorage.get(i).getTf().setMaxWidth(300);
            VBox connectPane = new VBox(10);
            connectPane.setAlignment(Pos.CENTER);
            connectPane.setAlignment(Pos.CENTER);
            connectPane.getChildren().addAll(labelandfieldStorage.get(i).getLb(),labelandfieldStorage.get(i).getTf());
            if(i == 0) {
                mainPane.setTop(connectPane);
            }else if(i == 1){
                mainPane.setCenter(connectPane);
            }
        }
        javafx.scene.control.Button btnSignin = new javafx.scene.control.Button("Sign In");
        btnSignin.getStylesheets().add(this.getClass().getResource("Css/ahk_main_bottombtns_css.css").toExternalForm());
        BorderPane.setAlignment(btnSignin,Pos.CENTER);
        btnSignin.setMaxSize(150,50);
        BorderPane btnPane = new BorderPane(btnSignin);
        btnPane.setPadding(new javafx.geometry.Insets(35,35,35,35));
        mainPane.setBottom(btnPane);
        createListeners(btnSignin,tfUsername,tfPassword);
    }
    public JFXPanel giveView(){
        return signInView;
    }
    /*
    * a quick shadowclass to store components in. To make code cleaner and easier to undestand by others.s
     */
    class components{
        public javafx.scene.control.Label lb;
        public javafx.scene.control.TextField tf;
        public components(javafx.scene.control.TextField tfield, javafx.scene.control.Label lbel){
            lb = lbel;
            tf = tfield;
        }
        public javafx.scene.control.Label getLb(){
            return lb;
        }
        public javafx.scene.control.TextField getTf(){
            return tf;
        }
    }
    private void createListeners(Button btnSignin, TextField tfUsername, PasswordField tfPassword){
        btnSignin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(profilesDb.checkPassword(tfUsername.getText(),tfPassword.getText())){
                    JOptionPane.showMessageDialog(viewStorage.mainFrame,"Successful!");
                    viewStorage.showBackwardsHideCurrent();
                    viewStorage.currentUserId = profilesDb.getProileIdByUsername(tfUsername.getText());
                }
            }
        });
    }
}
