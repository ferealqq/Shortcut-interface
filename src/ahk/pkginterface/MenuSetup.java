package ahk.pkginterface;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
        Label forwardsMenuLabel = new Label(">");
        Label backwardsMenuLabel = new Label("<");
        backAndForthActions(forwardsMenuLabel,backwardsMenuLabel);
        menuBar = new MenuBar();
        Menu forwardsMenu = new Menu();
        forwardsMenu.setGraphic(forwardsMenuLabel);
        Menu backwardsMenu = new Menu();
        backwardsMenu.setGraphic(backwardsMenuLabel);
        final Menu menuProfile = new Menu("Profile");
        menuItemsForProfile(menuProfile);
        final Menu menuHelp = new Menu("Help");
        final Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(backwardsMenu, forwardsMenu, menuProfile, menuHelp, menuView);
        rootPane.getChildren().addAll(menuBar);
        String menuBarCss = this.getClass().getResource("Css/main_menu_bar.css").toExternalForm();
        menuBar.getStylesheets().add(menuBarCss);
    }

    private void backAndForthActions(Label forwards, Label backwards) {
        forwards.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("forwards");
            }
        });
        backwards.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("backwards");
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

    public void hideSelectedAndShowSelected(JFXPanel hidethis, JFXPanel showthis) {
        hidethis.hide();
        showthis.show();
        viewHistoryBackwards.add(showthis);
        viewHistory.add(hidethis);
        mainFrame.add(showthis);
    }

    public static void main(String[] args) {
    }
}
