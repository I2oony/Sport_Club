package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    enum Role {
        SPORTSMAN,
        TRAINER;
    };

    public static void main(String[] args) {
        AtomicReference<String> host = new AtomicReference<>("localhost");
        AtomicInteger port = new AtomicInteger(3306);
        String database = "sport";

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
        settingsFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        settingsFrame.setSize(500,400);
        settingsFrame.setResizable(false);
        settingsFrame.setTitle("Спортивный клуб - настройки подключения");
        settingsFrame.setVisible(false);



        WelcomeScreen welcomeScreen = new WelcomeScreen();
        MainScreen mainScreen = new MainScreen();
        SettingsScreen settingsScreen = new SettingsScreen();

        ActionListener actions = e -> {
            String action = e.getActionCommand();
            System.out.println("Action performed: " + action);
            DatabaseConnect source = new DatabaseConnect(host.get(), port.get(), database);
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
                    mainScreen.clearData();
                    mainFrame.setContentPane(welcomeScreen.getScreen());
                    mainFrame.revalidate();
                    break;
                case "comboBoxChanged":
                    mainScreen.setTable();
                    break;
                case "openSettings":
                    mainFrame.setEnabled(false);
                    settingsScreen.setCurrentHostField(host.get());
                    settingsScreen.setCurrentPortField(port.toString());
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
                case "settingsSave":
                    settingsScreen.setErrorText(" ");
                    String hostField = settingsScreen.getHostField();
                    String portField = settingsScreen.getPortField();

                    int portValue = port.get();
                    String hostValue = host.get();
                    try {
                        if (!(hostField.equals("localhost") || hostField.equals("88.201.152.94"))) {
                            throw new Throwable();
                        }
                        hostValue = hostField;
                        portValue = Integer.parseInt(portField);
                    } catch (NumberFormatException exception) {
                        settingsScreen.addErrorText("Порт введён неверно! Допустимые значения: 0-65535");
                    } catch (Throwable textException) {
                        settingsScreen.addErrorText("Адрес введён неверно! Допустимые значения: '88.201.152.94' или 'localhost");
                    }
                    host.set(hostValue);
                    port.set(portValue);
                    settingsScreen.setCurrentHostField(host.get());
                    settingsScreen.setCurrentPortField(port.toString());
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
