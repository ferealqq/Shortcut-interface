package ahk.pkginterface.browsingFrames;

import ahk.pkginterface.database.ActionsDB;
import ahk.pkginterface.database.Actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class browseAction extends JFrame{

    private JPanel rootPane = new JPanel(new GridLayout(10, 1));
    private JTextField searchField = new JTextField();
    private ActionsDB actionsDB = new ActionsDB();

    public browseAction() {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(200, 600);
        setComponents();
    }

    public void setComponents() {
        rootPane.add(searchField);
        for (Actions action : actionsDB.getActions()) {
            JLabel actionLabel = new JLabel(action.getAction());
            actionLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }
            });
            rootPane.add(actionLabel);
        }
        this.add(new JScrollPane(rootPane));
    }
    public static void main(String[] args) {
        browseAction bAction = new browseAction();
        bAction.setVisible(true);

    }
}
