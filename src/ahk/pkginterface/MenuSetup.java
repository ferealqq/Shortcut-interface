package ahk.pkginterface;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Date;

public class MenuSetup {
    public Pane rootPane;
    public int currentUserId;
    public final ViewStorage viewStorage;

    private MenuBar menuBar;
    private Button forwardsMenuButton;
    private Button backwardsMenuButton;

    public MenuSetup(ViewStorage viewArchive, int id) {
        viewStorage = viewArchive;
        currentUserId = id;
    }

    public MenuBar createMenuBar() {
        menuBar = new MenuBar();
        forwardsMenuButton = new Button(">");
        backwardsMenuButton = new Button("<");
        backAndForthActions();
        disableOrEnable();
        Menu forwardsMenu = new Menu();
        forwardsMenu.setGraphic(forwardsMenuButton);
        Menu backwardsMenu = new Menu();
        backwardsMenu.setGraphic(backwardsMenuButton);
        final Menu menuProfile = new Menu("Profile");
        menuItemsForProfile(menuProfile);
        final Menu menuHelp = new Menu("Help");
        final Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(backwardsMenu, forwardsMenu, menuProfile, menuHelp, menuView);
        String menuBarCss = this.getClass().getResource("Css/main_menu_bar.css").toExternalForm();
        menuBar.getStylesheets().add(menuBarCss);
        return menuBar;
    }

    private void backAndForthActions() {
        disableOrEnable();
        this.forwardsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                viewStorage.showForwardHideCurrent();
            }
        });
        this.backwardsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                viewStorage.showBackwardsHideCurrent();
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
                    viewStorage.hideSelectedAndShowSelected(((JFXPanel)viewStorage.mainFrame.getContentPane().getComponent(viewStorage.mainFrame.getContentPane().getComponentCount() - 1)), viewStorage.viewMap.get("register"));
                }
            });
            profileMenu.getItems().addAll(loginItem,registerItem);
        }
    }
    public void disableOrEnable(){
        this.backwardsMenuButton.setDisable(viewStorage.viewHistory.isEmpty());
        this.forwardsMenuButton.setDisable(viewStorage.viewHistoryBackwards.isEmpty());
    }
    public static void main(String[] args) {
    }
}
