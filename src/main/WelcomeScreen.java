package main;

import javax.swing.*;
import java.awt.event.ActionListener;

public class WelcomeScreen {
    private JPanel rootPanel;
    private JButton loginAsSportsman;
    private JButton loginAsTrainer;
    private JPanel centerPanel;
    private JLabel titleLabel;
    private JButton settings;
    private JPanel loginButtonsPanel;

    public JPanel getScreen(){
        return rootPanel;
    }

    public void addActionListener(ActionListener actionListener) {
        loginAsSportsman.addActionListener(actionListener);
        loginAsTrainer.addActionListener(actionListener);
        settings.addActionListener(actionListener);
    }
}
