package ahk.pkginterface.commentFrames;

import javax.swing.*;
import java.awt.*;

public class commentFrame extends JFrame{
    public JLabel comment = new JLabel();
    public commentFrame() {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }
    public void addText(){
        this.add(comment, BorderLayout.CENTER);
        this.pack();
    }
    public static void main(String[] args) {
        commentFrame c = new commentFrame();
        c.setLocationRelativeTo(null);
        c.comment.setText("Username taken!");
        c.setVisible(true);
    }
}
