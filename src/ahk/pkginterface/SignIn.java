package ahk.pkginterface;

import ahk.pkginterface.database.ProfileDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SignIn extends JFrame {
    private JPanel rootPane = new JPanel(new GridLayout(4, 1));
    private JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel titlep = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel pwPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel loginPane = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private JLabel lbUsername = new JLabel("Username");
    private JLabel lbPassword = new JLabel("Password");
    private JLabel Register = new JLabel("                        Register                        ");

    private JTextField tfUsername = new JTextField(15);
    private JPasswordField tfPw = new JPasswordField(15);
    private JButton signin = new JButton("Sign in");
    private ProfileDB db = new ProfileDB();

    public SignIn(AHKInterface mainFrame) {
        GridLayout gap = (GridLayout) rootPane.getLayout();
        gap.setHgap(25);
        gap.setVgap(0);
        FlowLayout gaps = (FlowLayout) titlep.getLayout();
        gaps.setVgap(25);
        this.setTitle("SignIn");
        this.setSize(250, 320);
        this.setLocationRelativeTo(null);
        signin.setPreferredSize(new Dimension(80, 30));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setComponents();
        tfPw.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
                    if(db.checkPassword(tfPw.getText(),tfUsername.getText())) {
                        mainFrame.setCurrentUserId(db.getProileIdByUsername(tfUsername.getText()));
                        setVisible(false);
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(rootPane,"Something went wrong try again!");
                    }
                }
            }
        });
        signin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(db.checkPassword(tfPw.getText(),tfUsername.getText())) {
                    mainFrame.setCurrentUserId(db.getProileIdByUsername(tfUsername.getText()));
                    setVisible(false);
                    System.out.println("?");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(rootPane,"Something went wrong try again!");
                }
            }
        });
    }

    public static void main(String[] args) {
        new SignIn(new AHKInterface()).setVisible(true);
    }

    private void setComponents() {
        labelPane.add(lbUsername);
        labelPane.add(tfUsername);
        pwPane.add(lbPassword);
        pwPane.add(tfPw);
        loginPane.add(signin);
        loginPane.add(Register);
        rootPane.add(titlep);
        rootPane.add(labelPane);
        rootPane.add(pwPane);
        rootPane.add(loginPane);
        this.add(rootPane);
    }
}
