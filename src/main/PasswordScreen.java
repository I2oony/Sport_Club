package main;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PasswordScreen {
    private JPanel rootPanel;
    private JTextField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel passwordTitle;
    private JLabel passError;

    public JPanel getScreen() {
        return rootPanel;
    }

    public String getPasswordField() {
        return passwordField.getText();
    }

    public void addActionListener(ActionListener actionListener) {
        loginButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

    public void setErrorVisible (Boolean bool) {
        passError.setVisible(bool);
    }
}
