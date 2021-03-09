package main;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SettingsScreen {
    private JPanel rootPanel;
    private JLabel titleLabel;
    private JLabel screenTitleLabel;
    private JPanel settingsPanel;
    private JTextField portField;
    private JTextField hostField;
    private JButton cancelButton;
    private JButton saveButton;
    private JPanel buttonsPanel;
    private JButton loadFromFile;
    private JButton saveToFile;
    private JPanel toFileButtonsPanel;
    private JLabel hostLabel;
    private JLabel portLabel;

    public JPanel getScreen() {
        return rootPanel;
    }

    public void addActionListener(ActionListener actionListener) {
        saveButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }
}
