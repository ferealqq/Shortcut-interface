package ahk.pkginterface.ViewManagement;

import ahk.pkginterface.Shortcutinterface;
import ahk.pkginterface.Frames.*;
import ahk.pkginterface.Frames.BrowseAction;
import ahk.pkginterface.database.Key;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ComponentStorage {
    public Shortcutinterface shortcutinterface;
    public final KeySelection keySelection;
    public final ChangeKey changeKey;
    public final ChangeAction changeAction;
    public final Register register;
    public final TaskScheduler taskScheduler;
    public final BrowseAction browseaction;
    public final SignIn signIn;
    public final JFrame mainFrame;
    public final MenuSetup menuSetup;
    public final ScriptWriter scriptWriter;
    public final LinkedList<JFXPanel> viewHistory;
    public final LinkedList<JFXPanel> viewHistoryBackwards;
    public final HashMap<String, JFXPanel> viewMap;
    public int currentUserId;
    private Button firstStep;
    private Button secondStep;
    private Button thirdStep;
    public final ArrayList<Key> pressedKeys = new ArrayList<>();
    public final ArrayList<Key> toBeChangedKeys = new ArrayList<>();
    public final ArrayList<String> toBeChangedAction = new ArrayList<>();

    public final ArrayList<String> choosenActionName = new ArrayList<>();

    public final CurrentScriptInfo currentScriptInfo = new CurrentScriptInfo();

    public String nameofthescript;
    public final ArrayList<String> oldScriptPaths = new ArrayList<>();
    public String ahkRootPath = "";

    public ComponentStorage(JFrame main) {
        mainFrame = main;
        viewHistory = new LinkedList<>();
        viewMap = new HashMap<>();
        viewHistoryBackwards = new LinkedList<>();
        menuSetup = new MenuSetup(this);
        scriptWriter = new ScriptWriter(this);
        keySelection = new KeySelection(this);
        changeKey = new ChangeKey(this);
        changeAction = new ChangeAction(this);
        register = new Register(this);
        taskScheduler = new TaskScheduler(this);
        browseaction = new BrowseAction(this);
        signIn = new SignIn(this);
        createViewMap();
    }

    /*
    * Adding shortcutinterface(Homepage) to viewmap and giving componentstorage the variable shortcutinterface
     */
    public void setShortcutinterface(Shortcutinterface shortcutinterface) {
        this.shortcutinterface = shortcutinterface;
        viewMap.put("shortcutinterface", this.shortcutinterface.shortcutinterfaceview);
    }
    /*
    * Filling the viewmap with view
    * and adding them to the main jframe
     */

    public void createViewMap() {
        viewMap.put("keyselection",keySelection.keySelectionView);
        mainFrame.add(keySelection.keySelectionView);
        viewMap.put("changekey",changeKey.changeKeyView);
        mainFrame.add(changeKey.changeKeyView);
        viewMap.put("changeaction",changeAction.changeActionView);
        mainFrame.add(changeAction.changeActionView);
        viewMap.put("register", register.giveView());
        mainFrame.add(register.giveView());
        viewMap.put("browseaction", browseaction.giveView());
        mainFrame.add(browseaction.giveView());
        viewMap.put("signin", signIn.giveView());
        mainFrame.add(signIn.giveView());
        viewMap.put("taskscheduler", taskScheduler.giveView());
        mainFrame.add(taskScheduler.giveView());
    }
    /*
    * creating a tooltip and hacking the setup time.
    * Returns tooltip
     */
    public Tooltip createTooltip(String message) {
        final Tooltip tooltip = new Tooltip(message);
        hackTooltipStartTiming(tooltip);
        return tooltip;
    }
    /*
    * Hacks the tooltip to change popup time
     */
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
    /*
    * Hideing current frame and going forwards.
    * This function has been created for the purpose of the menusetup back and forward
     */
    public void showForwardHideCurrent() {
        if (!viewHistoryBackwards.isEmpty()) {
            JFXPanel last = viewHistoryBackwards.getLast();
            viewHistoryBackwards.removeLast();
            viewHistory.add((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1));
            ((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1)).hide();
            setMenuBars();
            last.show();
            mainFrame.add(last);
        }
    }
    /*
    * Hiding the current view and showing the last view.
    * This function has been created for the purpose of the menusetup back and forward
     */
    public void showBackwardsHideCurrent() {
        if (!viewHistory.isEmpty()) {
            JFXPanel last = viewHistory.getLast();
            viewHistoryBackwards.add(((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1)));
            viewHistory.removeLast();
            ((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1)).hide();
            setMenuBars();
            // kuin hideet jotain sinun pitää refreshaa menubar muista
            last.show();
            mainFrame.add(last);
        }
    }

    /*
    * Hides the first variable, and shows the second variable.
    * This function was made because I was hiding Jfxpanels alot and showing others so I thought this might be useful.
     */
    public void hideSelectedAndShowSelected(JFXPanel hidethis, JFXPanel showthis) {
        hidethis.hide();
        showthis.show();
        viewHistory.add(hidethis);
        mainFrame.add(showthis);
        setMenuBars();
    }
    public void setMenuBars(){
        setMenubar(register.rootPane);
        setMenubar(browseaction.topPane);
        setMenubar(shortcutinterface.menuPaneAkaRealRootPane);
        setMenubar(keySelection.rootPane);
        setMenubar(signIn.rootPane);
        setMenubar(taskScheduler.rootPane);
        setMenubar(changeKey.rootPane);
        setMenubar(changeAction.topPane);
    }
    /*
    *   setMenubar is a function that removes old menubar and creates a new menubar.
    *   Function was neseccary to make all scenes have the right components in it.
    *   Menubar has components which requires current information.
     */
    public void setMenubar(Pane currentViewsRootPane) {
        Iterator<Node> nodeIterator = currentViewsRootPane.getChildren().iterator();
        while (nodeIterator.hasNext()) {
            if (nodeIterator.next().getClass().equals(MenuBar.class)) {
                nodeIterator.remove();
            }
        }
        currentViewsRootPane.getChildren().add(0, menuSetup.createMenuBar());
    }
    /*
    * Creating step bar to keyselection, browsingaction and taskscheduler
     */
    public void createStepBar(Pane stepbarToThisPane) {
        BorderPane stepPane = new BorderPane();
        HBox centeredHBox = new HBox(35);
        firstStep = new Button("1");
        secondStep = new Button("2");
        thirdStep = new Button("3");
        createActionsToStepBar(firstStep, secondStep, thirdStep, stepbarToThisPane);
        centeredHBox.getChildren().addAll(firstStep, secondStep, thirdStep);
        centeredHBox.setAlignment(Pos.CENTER);
        String stepPaneCss = this.getClass().getResource("Css/stepPane.css").toExternalForm();
        stepPane.getStylesheets().add(stepPaneCss);
        stepPane.setCenter(centeredHBox);
        stepPane.setStyle("-fx-background-color:#F5F5F5");
        stepbarToThisPane.getChildren().add(stepPane);
    }
    /*
    * creating action event handlers on every button on the stepbar
     */
    private void createActionsToStepBar(Button firstStep, Button secondStep, Button thirdStep, Pane rootPane) {
        firstStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hideSelectedAndShowSelected((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1), viewMap.get("keyselection"));
            }
        });
        secondStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pressedKeys.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "You havent selected any keys try again later!");
                    return;
                }
                hideSelectedAndShowSelected((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1), viewMap.get("browseaction"));
            }
        });
        thirdStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (choosenActionName.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "You haven't selected any action go and  do that in the second step.");
                    return;
                }
                hideSelectedAndShowSelected((JFXPanel) mainFrame.getContentPane().getComponent(mainFrame.getContentPane().getComponentCount() - 1), viewMap.get("taskscheduler"));
            }
        });
    }
    /*
    * Highlighting the current step to make sure the user is in the right scene
     */
    public void highLightCurrentStep(int stepNumber) {
        switch (stepNumber) {
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
    /*
    * Finding where in the computer are ahk scripts. Making sure that we have all your ahkscripts.
    * So you can go back and edit them.
    * Findnig the paths for the scripts and saving them to an arraylist which can be referenced as oldScriptPahts
     */
    public void findAHKScripts() {
        File[] users = new File("C:\\users").listFiles();
        for (File user : users) {
            final File[] files = new File(user.getAbsolutePath()).listFiles();
            if(files != null) {
                searchFilesForAHKScriptAndAHKRoot(files);
            }
        }
    }
    /*
    * Just for making a loop of for
     */
    public void searchFilesForAHKScriptAndAHKRoot(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.listFiles() != null) searchFilesForAHKScriptAndAHKRoot(file.listFiles());
            } else {
                if (file.getAbsolutePath().endsWith(".ahk")) {
                    oldScriptPaths.add(file.getAbsolutePath());
                }else if(file.getAbsolutePath().endsWith("AutoHotkey.exe")){
                    ahkRootPath = file.getAbsolutePath();
                }else if(file.getAbsolutePath().contains("AutoHotkey.exe")){
                    ahkRootPath = file.getAbsolutePath();
                }
            }
        }
    }
    public void runThisScript(File ScriptToRun){
        String ahkPath = "C:\\Program Files\\AutoHotkey\\AutoHotkey.exe";
        File ahkPathFile = new File(ahkPath);
        if(!ahkPathFile.exists()) ahkPath = ahkRootPath;
        try {
            Runtime.getRuntime().exec(new String[] {ahkPath,ScriptToRun.getAbsolutePath(),"wutface"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
