package ahk.pkginterface;

import ahk.pkginterface.browsingFrames.browseAction;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;

public class ViewStorage {

    //public final Register register;
    //public final browseAction browseaction;
    public final JFrame mainFrame;
    public final MenuSetup menuSetup;
    public final LinkedList<JFXPanel> viewHistory;
    public final LinkedList<JFXPanel> viewHistoryBackwards;
    public final HashMap<String,JFXPanel> viewMap;
    public final int currentUserId;

    public ViewStorage(JFrame main,int userid){
        currentUserId = userid;
        mainFrame = main;
        viewHistory = new LinkedList<>();
        viewMap = new HashMap<>();
        viewHistoryBackwards = new LinkedList<>();
        menuSetup = new MenuSetup(mainFrame,viewMap,viewHistory,currentUserId);
        //register = new Register();
        //browseaction = new browseAction();
        createViewMap();
    }
    public void createViewMap(){
        //viewMap.put("register",register.giveView());
        //viewMap.put("browseaction",browseaction.giveView());
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
    private void createStepBar(Pane stepbarToThisPane){
        BorderPane stepPane = new BorderPane();
        HBox centeredHBox = new HBox(35);
        Button firstStep = new Button("1");
        Button secondStep = new Button("2");
        Button thirdStep = new Button("3");
        centeredHBox.getChildren().addAll(firstStep,secondStep,thirdStep);
        centeredHBox.setAlignment(Pos.CENTER);
        String stepPaneCss = this.getClass().getResource("Css/stepPane.css").toExternalForm();
        stepPane.getStylesheets().add(stepPaneCss);
        firstStep.setStyle("-fx-background-color:#A9A9A9");
        stepPane.setCenter(centeredHBox);
        stepPane.setStyle("-fx-background-color:#F5F5F5");
        stepbarToThisPane.getChildren().add(stepPane);
    }
}
