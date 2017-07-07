package ahk.pkginterface;

import ahk.pkginterface.commentFrames.commentFrame;
import ahk.pkginterface.database.ProfilesData;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;


public class Register{
    public final JFXPanel registerView = new JFXPanel();
    public final VBox rootPane = new VBox();

    private ProfilesData profilesDb = new ProfilesData();
    public final ViewStorage viewStorage;
    private String commentMsg;
    private commentFrame comment = new commentFrame();

    private final HashMap<String,TextField> textFieldComponentArchive = new HashMap<>();
    private final HashMap<String,EventHandler> eventHandlerArchive = new HashMap<>();

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
        createListeners();
        return (scene);
    }
    private void createComponents(){
        ArrayList<components> labelandfieldStorage = new ArrayList<>();
        Label lbUsername = new Label("Username");
        lbUsername.setWrapText(true);
        TextField tfUsername = new TextField();
        textFieldComponentArchive.put("tfUsername",tfUsername);
        labelandfieldStorage.add(new components(tfUsername,lbUsername));
        Label lbEmail = new Label("Email");
        TextField tfEmail = new TextField();
        labelandfieldStorage.add(new components(tfEmail,lbEmail));
        textFieldComponentArchive.put("tfEmail",tfEmail);
        Label lbPassword = new Label("Password");
        PasswordField tfPassword = new PasswordField();
        labelandfieldStorage.add(new components(tfPassword,lbPassword));
        Label lbPassword2 = new Label("Password again!");
        textFieldComponentArchive.put("tfPassword",tfPassword);
        PasswordField tfPassword2 = new PasswordField();
        textFieldComponentArchive.put("tfPassword2",tfPassword2);
        labelandfieldStorage.add(new components(tfPassword2,lbPassword2));
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
    private void createListeners(){
        EventHandler tfUsernameKeyReleased = new EventHandler<javafx.scene.input.KeyEvent>() {
            public void handle(javafx.scene.input.KeyEvent keyEvent) {
                TextField tfUsername = textFieldComponentArchive.get("tfUsername");
                if(!profilesDb.checkUsername(tfUsername.getText())){
                    commentMsg = "Username taken! Try something else.";
                    comment.comment.setText(commentMsg);
                    comment.addText();
                    comment.setVisible(true);
                    //tfUsername.setForeground(Color.red);
                    //comment.setLocation(getX()+210,getY()+57);
                    //tfUsername.addMouseListener(setHover);
                }else{
                    //tfUsername.setForeground(Color.BLACK);
                    //tfUsername.removeMouseListener(setHover);
                    commentMsg ="";
                    comment.comment.setText("");
                }
            }
        };
        eventHandlerArchive.put("tfUsernameKeyReleased",tfUsernameKeyReleased);
    }
    public static void main(String[] args) {
    }
}
