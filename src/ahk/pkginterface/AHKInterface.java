package ahk.pkginterface;

import ahk.pkginterface.database.ProfileDB;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

public class AHKInterface extends JFrame {

    private JPanel pohja = new JPanel(new BorderLayout());
    private JPanel keyboard = new JPanel(new GridLayout(6, 1));
    private JPanel bottomPane = new JPanel(new GridLayout(1,6));

    private JButton btscripts = new JButton("Open");
    private JButton btsignin = new JButton("Sign in");
    private JButton btdetect = new JButton("Detect");
    private JButton btundo = new JButton("Undo");
    private JButton btbrowse = new JButton("Browse");
    private JButton btcommit = new JButton("Publish");
    private JButton bthelp = new JButton("Help");
    private JButton btnext = new JButton("Next");

    private ArrayList<String> newhotkeys = new ArrayList<>();

    public AHKInterface() {
        this.setTitle("AHK-Interface");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        asetteleKomponentit();
        this.setResizable(false);
    }

    private void setKeyboard() {
        int row = 0;
        KeyData keys = new KeyData();
        while (row <= keys.rows.size() - 1) {
            JPanel rowPane = new JPanel(new GridLayout(1, keys.rows.get(row).length));
            for (String currentKey : keys.rows.get(row)) {
                JButton key = new JButton(currentKey);
                key.setForeground(Color.GRAY);
                key.setBackground(Color.black);
                key.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (!key.getBackground().equals(Color.white)) {
                                key.setBackground(Color.white);
                            } else {
                                key.setBackground(Color.BLACK);
                            }
                            newhotkeys.add(currentKey);
                        }
                    }
                });
                rowPane.add(key);
            }
            keyboard.add(rowPane);
            row++;
        }
    }

    public static void main(String[] args) {
        new AHKInterface().setVisible(true);
    }

    private void asetteleKomponentit() {
        bottomPane.add(btscripts);
        bottomPane.add(btsignin);
        bottomPane.add(btdetect);
        bottomPane.add(btundo);
        bottomPane.add(btbrowse);
        bottomPane.add(btcommit);
        bottomPane.add(bthelp);
        bottomPane.add(btnext);

        pohja.add(keyboard, BorderLayout.CENTER);
        pohja.add(bottomPane, BorderLayout.PAGE_END);
        this.add(pohja);
        setKeyboard();
    }
}
