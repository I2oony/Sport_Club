package main;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionListener;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class SettingsScreen {
    private JPanel rootPanel;
    private JLabel titleLabel;
    private JLabel screenTitleLabel;
    private JPanel settingsPanel;
    private JFormattedTextField portField;
    private JTextField hostField;
    private JButton cancelButton;
    private JButton saveButton;
    private JPanel buttonsPanel;
    private JButton loadFromFile;
    private JButton saveToFile;
    private JPanel toFileButtonsPanel;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JTextArea errorMessage;
    private JTextField currentHostField;
    private JTextField currentPortField;
    private JLabel currentValue;
    private JLabel newValues;

    public JPanel getScreen() {
        return rootPanel;
    }

    public void addActionListener(ActionListener actionListener) {
        saveButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

    public String getHostField() {
        return hostField.getText();
    }
    public void setCurrentHostField(String text) {
        currentHostField.setText(text);
    }

    public String getPortField() {
        return portField.getText();
    }
    public void setCurrentPortField(String text) {
        currentPortField.setText(text);
    }

    public void setErrorText(String text) {
        errorMessage.setText(text);
    }

    public void addErrorText(String text) {
        if (errorMessage.getText().equals(" ")){
            errorMessage.setText(text);
        } else {
            errorMessage.setText(errorMessage.getText() + "\n" + text);
        }
    }
}
