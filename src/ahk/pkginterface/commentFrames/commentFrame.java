package ahk.pkginterface.commentFrames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class commentFrame extends JFrame{
    public JLabel comment = new JLabel();
    public commentFrame(JFrame superFrame) {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setFocusableWindowState(false);
    }
    public void addText(){
        this.add(comment, BorderLayout.CENTER);
        this.pack();
    }
    public static void main(String[] args) {
    }
}
