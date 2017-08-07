package ahk.pkginterface;

import ahk.pkginterface.ViewManagement.ComponentStorage;
import ahk.pkginterface.database.Key;
import ahk.pkginterface.database.KeyData;
import ahk.pkginterface.database.Keys;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.*;

import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;


public class AHKInterface extends JFrame {
    public final JFXPanel ahkinterfaceView = new JFXPanel();
    public final VBox rootPane = new VBox();

    public final JFrame main = this;
    public ComponentStorage componentStorage;// siirä viewmap aloitus formiin sitten kuin se on tehty

    public AHKInterface() {
        componentStorage = new ComponentStorage(main);
        componentStorage.setAhkinterface(this);
        constructAHK();
    }

    private void constructAHK() {
        this.add(ahkinterfaceView);
        this.setVisible(true);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        initComponents(ahkinterfaceView);
        componentStorage.findAHKScripts();
    }

    private void initComponents(JFXPanel jfxPanel) {
        Scene scene = createScene();
        jfxPanel.setScene(scene);
    }
    private Scene createScene() {
        Scene scene = new Scene(rootPane, 1000, 600);
        rootPane.getChildren().add(componentStorage.menuSetup.createMenuBar());
        
        return (scene);
    }

    public static void main(String[] args) {
        new AHKInterface();
    }
}
