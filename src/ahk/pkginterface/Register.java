package ahk.pkginterface;

import ahk.pkginterface.database.ProfileDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Register extends JFrame{
    private JPanel rootPane = new JPanel(new GridLayout(5, 1));
    private JPanel usernamePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel pwPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel pwPane2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel emailPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel registerPane = new JPanel(new FlowLayout(FlowLayout.CENTER));

    private JLabel lbUsername = new JLabel("Username");
    private JLabel lbEmail = new JLabel("     Email     ");
    private JLabel lbPassword = new JLabel("Password");
    private JLabel lbPassword2 = new JLabel("Re-enter password");

    private JTextField tfUsername = new JTextField(15);
    private JTextField tfEmail = new JTextField(15);
    private JPasswordField tfPw = new JPasswordField(15);
    private JPasswordField tfPw2 = new JPasswordField(15);

    private JButton btRegister = new JButton("Register");
    private JButton btBack = new JButton("Back");

    private AHKInterface mainFrame;
    private SignIn signInFrame;
    private ProfileDB db = new ProfileDB();

    private MouseAdapter hoverUsername = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    public Register(AHKInterface mainForm,SignIn signInForm) {
        mainFrame = mainForm;
        signInFrame = signInForm;
        this.setSize(250, 350);
        this.setLocationRelativeTo(null);
        btRegister.setPreferredSize(new Dimension(90, 30));
        btBack.setPreferredSize(new Dimension(90, 30));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setComponents();
        tfEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(db.checkUsername(tfUsername.getText())) tfUsername.setForeground(Color.red);
            }
        });
        btRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tfPw.getText().equals(tfPw2.getText())) {
                    if(!db.createUser(tfUsername.getText(), tfPw.getText(), tfEmail.getText())) {
                        JOptionPane.showMessageDialog(rootPane,"Something went wrong!");
                        tfPw.setText("");
                        tfPw2.setText("");
                        tfEmail.setText("");
                        tfUsername.setText("");
                        return;
                    }
                    if(mainFrame.currentUserId==0){
                        mainFrame.setCurrentUserId(db.getProileIdByUsername(tfUsername.getText()));
                        System.out.println(mainFrame.currentUserId);
                    }
                    dispose();
                    mainFrame.setVisible(true);
                    JOptionPane.showMessageDialog(rootPane,"Successful!");
                }
            }
        });
        btBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signInFrame.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new Register(new AHKInterface(),new SignIn(new AHKInterface())).setVisible(true);
    }

    private void setComponents() {
        usernamePane.add(lbUsername);
        usernamePane.add(tfUsername);

        emailPane.add(lbEmail);
        emailPane.add(tfEmail);

        pwPane.add(lbPassword);
        pwPane.add(tfPw);

        pwPane2.add(lbPassword2);
        pwPane2.add(tfPw2);

        registerPane.add(btBack);
        registerPane.add(btRegister);

        rootPane.add(usernamePane);
        rootPane.add(emailPane);
        rootPane.add(pwPane);
        rootPane.add(pwPane2);
        rootPane.add(registerPane);

        this.add(rootPane);
    }
}
