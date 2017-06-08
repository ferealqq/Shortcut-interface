package ahk.pkginterface.browsingFrames;

import ahk.pkginterface.database.ActionsData;
import ahk.pkginterface.database.Actions;
import ahk.pkginterface.commentFrames.commentFrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class browseAction extends JFrame{

    private JPanel rootPane = new JPanel(new GridLayout(10, 1));
    private JTextField searchField = new JTextField();
    private ActionsData actionsDB = new ActionsData();

    private ArrayList<JLabel> currentComponentArchive = new ArrayList<>();

    public browseAction() {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(200, 600);
        setComponents();
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                for (JLabel deletedComponent:currentComponentArchive) {
                    rootPane.remove(deletedComponent);
                }
                rootPane.revalidate();
                rootPane.repaint();
                currentComponentArchive.removeAll(currentComponentArchive);
                searchField.requestFocus();

                String searchWords = searchField.getText();
                setActionChoices(actionsDB.searchAction(searchWords));
            }
        });

    }
    public void setActionChoices(ArrayList<Actions> acc){
        for (Actions action : acc){
            commentFrame CommentFrame = new commentFrame();
            JLabel actionLabel = new JLabel(action.getAction());
            actionLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    CommentFrame.comment.setText("fug memes");
                    CommentFrame.addText();
                    CommentFrame.setVisible(true);
                    CommentFrame.setLocationRelativeTo(actionLabel);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    CommentFrame.setVisible(false);
                }
            });
            currentComponentArchive.add(actionLabel);
            rootPane.add(actionLabel);
        }
    }
    public void setComponents() {
        rootPane.add(searchField);
        setActionChoices(actionsDB.getActions());
        this.add(new JScrollPane(rootPane));
    }
    public static void main(String[] args) {
        browseAction bAction = new browseAction();
        bAction.setVisible(true);

    }
}
