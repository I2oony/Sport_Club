package main;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PasswordScreen {
    private JPanel rootPanel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel passwordTitle;
    private JLabel passError;

    public JPanel getScreen() {
        passwordField.setText("");
        return rootPanel;
    }

    public String getPasswordField() {
        char[] input = passwordField.getPassword();
        String pass = "";
        for (int i=0; i< input.length; i++) {
            pass = pass.concat(String.valueOf(input[i]));
        }
        return pass;
    }

    public void addActionListener(ActionListener actionListener) {
        loginButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

    public void setErrorVisible (Boolean bool) {
        passError.setVisible(bool);
    }
}
