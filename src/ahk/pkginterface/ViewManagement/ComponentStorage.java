package ahk.pkginterface.ViewManagement;

import ahk.pkginterface.AHKInterface;
import ahk.pkginterface.Frames.Register;
import ahk.pkginterface.Frames.SignIn;
import ahk.pkginterface.Frames.TaskScheduler;
import ahk.pkginterface.browsingFrames.browseAction;
import ahk.pkginterface.database.Key;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ComponentStorage {
    public AHKInterface ahkinterface;
    public final Register register;
    public final TaskScheduler taskScheduler;
    public final browseAction browseaction;
    public final SignIn signIn;
    public final JFrame mainFrame;
    public final MenuSetup menuSetup;
    public final LinkedList<JFXPanel> viewHistory;
    public final LinkedList<JFXPanel> viewHistoryBackwards;
    public final HashMap<String,JFXPanel> viewMap;
    public int currentUserId;
    private Button firstStep;
    private Button secondStep;
    private Button thirdStep;
    public final ArrayList<Key> pressedKeys = new ArrayList<>();

    public final ArrayList<String> choosenActionPath = new ArrayList<>();

    public String nameofthescript;

    public ComponentStorage(JFrame main){
        mainFrame = main;
        viewHistory = new LinkedList<>();
        viewMap = new HashMap<>();
        viewHistoryBackwards = new LinkedList<>();
        menuSetup = new MenuSetup(this);
        register = new Register(this);
        taskScheduler = new TaskScheduler(this);
        browseaction = new browseAction(this);
        signIn = new SignIn(this);
        createViewMap();
    }
    /*
    * kuin uusi main form on valmis lisää tänne ahkinterface = new AHKinterface();
     */
    public void setAhkinterface(AHKInterface ahkinterface){
        this.ahkinterface = ahkinterface;
        viewMap.put("ahkinterface",this.ahkinterface.ahkinterfaceView);
    }
    public void createViewMap(){
        viewMap.put("register",register.giveView());
        mainFrame.add(register.giveView());
        viewMap.put("browseaction",browseaction.giveView());
        mainFrame.add(browseaction.giveView());
        viewMap.put("signin",signIn.giveView());
        mainFrame.add(signIn.giveView());
        viewMap.put("taskscheduler",taskScheduler.giveView());
        mainFrame.add(taskScheduler.giveView());
    }
    public Tooltip createTooltip(String message){
        final Tooltip tooltip = new Tooltip(message);
        hackTooltipStartTiming(tooltip);
        return tooltip;
    }
    private static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showForwardHideCurrent(){
        if(!viewHistoryBackwards.isEmpty()){
            JFXPanel last = viewHistoryBackwards.getLast();
            viewHistoryBackwards.removeLast();
            viewHistory.add((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1));
            ((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1)).hide();
            setMenubar(register.rootPane);
            setMenubar(browseaction.topPane);
            setMenubar(ahkinterface.rootPane);
            setMenubar(signIn.rootPane);
            setMenubar(taskScheduler.rootPane);
            last.show();
            mainFrame.add(last);
        }
    }
    public void showBackwardsHideCurrent(){
        if(!viewHistory.isEmpty()){
            JFXPanel last = viewHistory.getLast();
            viewHistoryBackwards.add(((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1)));
            viewHistory.removeLast();
            ((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1)).hide();
            setMenubar(register.rootPane);
            setMenubar(browseaction.topPane);
            setMenubar(ahkinterface.rootPane);
            setMenubar(signIn.rootPane);
            setMenubar(taskScheduler.rootPane);
            // kuin hideet jotain sinun pitää refreshaa menubar muista
            last.show();
            mainFrame.add(last);
        }
    }
    public void hideSelectedAndShowSelected(JFXPanel hidethis, JFXPanel showthis) {
        hidethis.hide();
        showthis.show();
        viewHistory.add(hidethis);
        mainFrame.add(showthis);
        setMenubar(register.rootPane);
        setMenubar(browseaction.topPane);
        setMenubar(ahkinterface.rootPane);
        setMenubar(signIn.rootPane);
        setMenubar(taskScheduler.rootPane);
    }
    public void setMenubar(Pane currentViewsRootPane){
        Iterator<Node> nodeIterator = currentViewsRootPane.getChildren().iterator();
        while(nodeIterator.hasNext()){
            if(nodeIterator.next().getClass().equals(MenuBar.class)){
                nodeIterator.remove();
            }
        }
        currentViewsRootPane.getChildren().add(0,menuSetup.createMenuBar());
        //register.mainPane.getChildren() sinun pitää hakea oikea mainPane original muodossa että voit muokata sitä
    }
    public void createStepBar(Pane stepbarToThisPane){
        BorderPane stepPane = new BorderPane();
        HBox centeredHBox = new HBox(35);
        firstStep = new Button("1");
        secondStep = new Button("2");
        thirdStep = new Button("3");
        createActionsToStepBar(firstStep,secondStep,thirdStep,stepbarToThisPane);
        centeredHBox.getChildren().addAll(firstStep,secondStep,thirdStep);
        centeredHBox.setAlignment(Pos.CENTER);
        String stepPaneCss = this.getClass().getResource("Css/stepPane.css").toExternalForm();
        stepPane.getStylesheets().add(stepPaneCss);
        stepPane.setCenter(centeredHBox);
        stepPane.setStyle("-fx-background-color:#F5F5F5");
        stepbarToThisPane.getChildren().add(stepPane);
    }
    private void createActionsToStepBar(Button firstStep, Button secondStep, Button thirdStep,Pane rootPane){
        firstStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hideSelectedAndShowSelected((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1),viewMap.get("ahkinterface"));
            }
        });
        secondStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pressedKeys.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "You havent selected any keys try again later!");
                    return;
                }
                hideSelectedAndShowSelected((JFXPanel)mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1),viewMap.get("browseaction"));
            }
        });
        thirdStep.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if(choosenActionPath.isEmpty()){
                    JOptionPane.showMessageDialog(mainFrame,"You haven't selected any action go and  do that in the second step.");
                    return;
                }
                hideSelectedAndShowSelected((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount()-1), viewMap.get("taskscheduler"));
            }
        });
    }
    public void highLightCurrentStep(int stepNumber){
        switch(stepNumber){
            case 1:
                firstStep.setStyle("-fx-background-color:#A9A9A9");
                secondStep.setStyle("-fx-background-color:#D3D3D3;");
                thirdStep.setStyle("-fx-background-color:#D3D3D3;");
                break;
            case 2:
                firstStep.setStyle("-fx-background-color:#D3D3D3;");
                secondStep.setStyle("-fx-background-color:#A9A9A9");
                thirdStep.setStyle("-fx-background-color:#D3D3D3;");
                break;
            case 3:
                firstStep.setStyle("-fx-background-color:#D3D3D3;");
                secondStep.setStyle("-fx-background-color:#D3D3D3;");
                thirdStep.setStyle("-fx-background-color:#A9A9A9");
                break;
        }
    }
}
