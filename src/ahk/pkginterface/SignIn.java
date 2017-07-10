package ahk.pkginterface;

import ahk.pkginterface.database.ProfilesData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignIn extends JFrame {
    private JPanel rootPane = new JPanel(new GridLayout(4, 1));
    private JPanel labelPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel pwPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel loginPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private JLabel lbUsername = new JLabel("Username");
    private JLabel lbPassword = new JLabel("Password");
    private JLabel Register = new JLabel("                        Register                        ");
    private JLabel lbBack = new JLabel("Back");

    private JTextField tfUsername = new JTextField(15);
    private JPasswordField tfPw = new JPasswordField(15);
    private JButton signin = new JButton("Sign in");

    private Register registerFrame;
    private ProfilesData db = new ProfilesData();


    public SignIn(AHKInterface mainFrame) {
        //registerFrame = new Register(mainFrame,this);
        this.setTitle("SignIn");
        this.setSize(230, 260);
        this.setLocationRelativeTo(null);
        signin.setPreferredSize(new Dimension(80, 30));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setComponents();
        tfPw.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
                    if(db.checkPassword(tfPw.getText(),tfUsername.getText())) {
                        //mainFrame.setCurrentUserId(db.getProileIdByUsername(tfUsername.getText()));
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
                    //mainFrame.setCurrentUserId(db.getProileIdByUsername(tfUsername.getText()));
                    setVisible(false);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(rootPane,"Something went wrong try again!");
                }
            }
        });
        Register.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
            }
        });
        lbBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //mainFrame.setVisible(true);
                setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        new SignIn(new AHKInterface()).setVisible(true);
    }

    private void setComponents() {
        //lbBack.setSize(new Dimension());
        labelPane.add(lbUsername);
        labelPane.add(tfUsername);
        pwPane.add(lbPassword);
        pwPane.add(tfPw);
        loginPane.add(signin);
        loginPane.add(Register);
        bottomPane.add(lbBack);
        rootPane.add(labelPane);
        rootPane.add(pwPane);
        rootPane.add(loginPane);
        rootPane.add(bottomPane);
        this.add(rootPane);
    }
}
