package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    enum Role {
        SPORTSMAN,
        TRAINER;
    };

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point center = new Point();

        center.x = screenSize.width / 2;
        center.y = screenSize.height / 2;

        Dimension windowSize = new Dimension();
        windowSize.height = 720;
        windowSize.width = 1280;

        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainFrame.setMinimumSize(new Dimension(800, 600));
        mainFrame.setSize(windowSize);
        mainFrame.setResizable(true);
        mainFrame.setTitle("Спортивный клуб");

        mainFrame.setLocation(center.x - windowSize.width / 2, center.y - windowSize.height / 2);

        JFrame settingsFrame = new JFrame();
        settingsFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        settingsFrame.setSize(500,400);
        settingsFrame.setResizable(false);
        settingsFrame.setTitle("Спортивный клуб - настройки подключения");
        settingsFrame.setVisible(false);

        DatabaseConnect source = new DatabaseConnect("localhost", 3306, "sport");

        WelcomeScreen welcomeScreen = new WelcomeScreen();
        MainScreen mainScreen = new MainScreen();
        SettingsScreen settingsScreen = new SettingsScreen();

        ActionListener actions = e -> {
            String action = e.getActionCommand();
            System.out.println("Action performed: " + action);
            switch (action) {
                case "openSportsmanScreen":
                    mainScreen.setRole(Role.SPORTSMAN);
                    source.setCredentials("sportsman", "");
                    mainScreen.setDatabaseConnect(source);
                    mainFrame.setContentPane(mainScreen.getScreen());
                    mainFrame.revalidate();
                    break;
                case "openTrainerScreen":
                    mainScreen.setRole(Role.TRAINER);
                    source.setCredentials("trainer", "TrainerPassword");
                    mainScreen.setDatabaseConnect(source);
                    mainFrame.setContentPane(mainScreen.getScreen());
                    mainFrame.revalidate();
                    break;
                case "openWelcomeScreen":
                    mainFrame.setContentPane(welcomeScreen.getScreen());
                    mainFrame.revalidate();
                    break;
                case "comboBoxChanged":
                    mainScreen.setTable();
                    break;
                case "openSettings":
                    mainFrame.setEnabled(false);
                    settingsFrame.setLocation(new Point(
                            mainFrame.getX()+mainFrame.getWidth()/2-settingsFrame.getWidth()/2,
                            mainFrame.getY()+mainFrame.getHeight()/2-settingsFrame.getHeight()/2));
                    settingsFrame.setVisible(true);
                    break;
                case "settingsCancel":
                    settingsFrame.setVisible(false);
                    mainFrame.setVisible(true);
                    mainFrame.setEnabled(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value at action switch: " + action);
            }
        };

        welcomeScreen.addActionListener(actions);
        mainScreen.addActionListener(actions);
        settingsScreen.addActionListener(actions);

        settingsFrame.setContentPane(settingsScreen.getScreen());

        mainFrame.setContentPane(welcomeScreen.getScreen());
        mainFrame.setVisible(true);
    }
}
