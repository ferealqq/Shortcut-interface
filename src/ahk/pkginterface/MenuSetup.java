package ahk.pkginterface;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Objects;

public class MenuSetup {
    public Pane rootPane;
    public int currentUserId;
    public Register registerFrame;
    private LinkedList viewHistory;
    private JFrame mainFrame;
    public MenuSetup(JFrame mainForm,LinkedList viewHis,BorderPane root,int id) {
        this.registerFrame = new Register(mainFrame);
        viewHistory = viewHis;
        mainFrame = mainForm;
        rootPane = root;
        currentUserId = id;
        createMenuBar();
    }
    public MenuSetup(JFrame mainForm,LinkedList viewHis,VBox root,int id) {
        mainFrame = mainForm;
        this.registerFrame = new Register(mainFrame);
        viewHistory = viewHis;
        rootPane = root;
        currentUserId = id;
        createMenuBar();
    }
    public MenuSetup(HBox root,int id) {
        rootPane = root;
        currentUserId = id;
        createMenuBar();
    }
    public void createMenuBar() {
        MenuBar menuBar = new MenuBar();
        final Menu forwardsMenu = new Menu(">");
        final Menu backwardsMenu = new Menu("<");
        final Menu menuProfile = new Menu("Profile");
        menuItemsForProfile(menuProfile);
        final Menu menuHelp = new Menu("Help");
        final Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(backwardsMenu,forwardsMenu,menuProfile, menuHelp, menuView);
        rootPane.getChildren().addAll(menuBar);
        String menuBarCss = this.getClass().getResource("Css/main_menu_bar.css").toExternalForm();
        menuBar.getStylesheets().add(menuBarCss);
    }
    private void menuItemsForProfile(Menu profileMenu){
        if(0<currentUserId){
           MenuItem logoutItem = new MenuItem("Log Out");
           logoutItem.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   currentUserId = 0;
               }
           });
           profileMenu.getItems().add(logoutItem);
        }else{
            MenuItem loginItem = new MenuItem("Log in");
            loginItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                }
            });
            MenuItem registerItem = new MenuItem("Register");
            registerItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println(viewHistory.getLast());
                    ((JFXPanel)viewHistory.getLast()).hide();
                    mainFrame.add(registerFrame.giveView());
                }
            });
            profileMenu.getItems().add(registerItem);
        }
    }

    public static void main(String[] args) {
    }
}
