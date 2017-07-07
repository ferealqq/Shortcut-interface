package ahk.pkginterface;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class MenuSetup {
    public Pane rootPane;
    public int currentUserId;
    public Register registerFrame;
    public LinkedList<JFXPanel> viewHistory;
    public LinkedList<JFXPanel> viewHistoryBackwards = new LinkedList<>();
    public JFrame mainFrame;
    public HashMap<String, JFXPanel> viewMap;

    private MenuBar menuBar;
    private Button forwardsMenuButton;
    private Button backwardsMenuButton;

    public MenuSetup(JFrame main, HashMap viewmap, LinkedList viewHis, int id) {
        mainFrame = main;
        viewHistory = viewHis;
        viewMap = viewmap;
        currentUserId = id;
    }

    public void setRootPane(BorderPane root) {
        rootPane = root;
        createMenuBar();
    }

    public void setRootPane(VBox root) {
        rootPane = root;
        createMenuBar();
    }

    public void setRootPane(HBox root) {
        rootPane = root;
        createMenuBar();
    }
    public void createMenuBar() {
        forwardsMenuButton = new Button(">");
        backwardsMenuButton = new Button("<");
        backAndForthActions();
        disableOrEnable();
        menuBar = new MenuBar();
        Menu forwardsMenu = new Menu();
        forwardsMenu.setGraphic(forwardsMenuButton);
        Menu backwardsMenu = new Menu();
        backwardsMenu.setGraphic(backwardsMenuButton);
        final Menu menuProfile = new Menu("Profile");
        menuItemsForProfile(menuProfile);
        final Menu menuHelp = new Menu("Help");
        final Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(backwardsMenu, forwardsMenu, menuProfile, menuHelp, menuView);
        rootPane.getChildren().addAll(menuBar);
        String menuBarCss = this.getClass().getResource("Css/main_menu_bar.css").toExternalForm();
        menuBar.getStylesheets().add(menuBarCss);
    }
    public JFXPanel getLastView(){
        if(!viewHistory.isEmpty()){
            JFXPanel last = viewHistory.getLast();
            viewHistoryBackwards.add(((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1)));
            viewHistory.removeLast();
            return last;
        }
        return null;
    }

    private void backAndForthActions() {
        disableOrEnable();
        this.forwardsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hideSelectedAndShowSelected(((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1)),viewHistoryBackwards.getLast());
            }
        });
        this.backwardsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hideSelectedAndShowSelected(((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1)),getLastView());
            }
        });
    }

    private void menuItemsForProfile(Menu profileMenu) {
        if (0 < currentUserId) {
            MenuItem logoutItem = new MenuItem("Log Out");
            logoutItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentUserId = 0;
                }
            });
            profileMenu.getItems().add(logoutItem);
        } else {
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
                    hideSelectedAndShowSelected(((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1)), viewMap.get("register"));
                }
            });
            profileMenu.getItems().add(registerItem);
        }
    }
    public void disableOrEnable(){
        this.backwardsMenuButton.setDisable(viewHistory.isEmpty());
        this.forwardsMenuButton.setDisable(viewHistoryBackwards.isEmpty());
    }
    public void hideSelectedAndShowSelected(JFXPanel hidethis, JFXPanel showthis) {
        hidethis.hide();
        showthis.show();
        System.out.println("hide");
        for (Node node:showthis.getScene().getRoot().getChildrenUnmodifiable()) {
            if(node.getClass().equals(MenuBar.class)){
                // keksi miten saat poistettua vanhan menubaarin
                // idea tee sillain että kuin käyttäjä on uudessa ruudussa ja liikuttaa hiirtä tai painaa näppäintä tai hoveraa. käytänössä tekee mitä vain. niin uudelleen runnat methodin disableorenable
            }
        }
        if(showthis.getScene().getRoot().getChildrenUnmodifiable().contains(MenuBar.class)) System.out.println("contains");
        //setRootPane((VBox)showthis.getScene().getRoot());
        viewHistory.add(hidethis);
        mainFrame.add(showthis);
    }

    public static void main(String[] args) {
    }
}
