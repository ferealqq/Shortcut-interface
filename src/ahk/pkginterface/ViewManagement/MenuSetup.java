package ahk.pkginterface.ViewManagement;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.ProfilesData;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MenuSetup {
    public Pane rootPane;
    public final ComponentStorage componentStorage;

    private MenuBar menuBar;
    private Button forwardsMenuButton;
    private Button backwardsMenuButton;

    public MenuSetup(ComponentStorage viewArchive) {
        componentStorage = viewArchive;
    }
    private MenuItem usernameMenuItem;
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
        menuBar.getMenus().addAll(backwardsMenu, forwardsMenu, menuProfile);
        String menuBarCss = this.getClass().getResource("Css/main_menu_bar.css").toExternalForm();
        menuBar.getStylesheets().add(menuBarCss);
        return menuBar;
    }

    private void backAndForthActions() {
        disableOrEnable();
        this.forwardsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                componentStorage.showForwardHideCurrent();
            }
        });
        this.backwardsMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                componentStorage.showBackwardsHideCurrent();
            }
        });
    }

    private void createCurrentUserNameItem(Menu profileMenu){
        usernameMenuItem = new MenuItem("Signed in as "+new ProfilesData().getUsernameById(componentStorage.currentUserId));
        profileMenu.getItems().add(usernameMenuItem);
    }

    private void menuItemsForProfile(Menu profileMenu) {
        if (0 < componentStorage.currentUserId) {
            createLogoutItem(profileMenu);
        } else {
            createSigninitem(profileMenu);
        }
        MenuItem registerItem = new MenuItem("Register");
        registerItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.hideSelectedAndShowSelected(((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1)), componentStorage.viewMap.get("register"));
            }
        });
        profileMenu.getItems().add(registerItem);
        if(0 < componentStorage.currentUserId)  createCurrentUserNameItem(profileMenu);
    }

    private void createSigninitem(Menu profileMenu) {
        MenuItem signInItem = new MenuItem("Sign in");
        signInItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.hideSelectedAndShowSelected(((JFXPanel) componentStorage.mainFrame.getContentPane().getComponent(componentStorage.mainFrame.getContentPane().getComponentCount() - 1)), componentStorage.viewMap.get("signin"));
            }
        });
        profileMenu.getItems().add(0,signInItem);
    }
    private void createLogoutItem(Menu profileMenu){
        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                componentStorage.currentUserId = 0;
                profileMenu.getItems().remove(logoutItem);
                profileMenu.getItems().remove(usernameMenuItem);
                createSigninitem(profileMenu);
            }
        });
        profileMenu.getItems().add(0,logoutItem);
    }
    /*
    * Disabling or enabling backwards and forwards menu button, if there is nothing to go back for or if there is nothing to go forward for.
     */
    public void disableOrEnable(){
        this.backwardsMenuButton.setDisable(componentStorage.viewHistory.isEmpty());
        this.forwardsMenuButton.setDisable(componentStorage.viewHistoryBackwards.isEmpty());
    }
}
