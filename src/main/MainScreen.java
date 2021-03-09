package main;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.Main.Role;

public class MainScreen {
    private JPanel rootPanel;
    private JPanel topPanel;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JComboBox<String> tableSelector;
    private JTable tableView;
    private JScrollPane scrollPane;
    private JLabel titleLabel;
    private JPanel bottomPanel;
    private JLabel titleSelector;
    private JLabel loggedAs;
    private JLabel userRole;
    private JPanel selector;
    private JPanel titlePanel;

    Role role;
    DatabaseConnect databaseConnect;
    Connection connect;

    public JPanel getScreen(){
        System.out.println("Current role: " + role);
        return rootPanel;
    }

    public void setRole(Role r){
        role = r;
        if (r.equals(Role.SPORTSMAN)) {
            userRole.setText("Спортсмен");
        } else {
            userRole.setText("Тренер");
        }
    }

    public void setDatabaseConnect (DatabaseConnect databaseConnect) {
        try {
            this.databaseConnect = databaseConnect;
            connect = this.databaseConnect.getConnection();
            System.out.println("Database successfully connected!");

            Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "select table_name from information_schema.TABLES where TABLE_SCHEMA='sport'";

            ResultSet results = statement.executeQuery(query);

            results.last();
            int rows = results.getRow();
            results.first();

            tableSelector.removeAllItems();

            for (int i = 0; i < rows; i++) {
                String tableName = results.getString(1);
                tableSelector.addItem(tableName);
                results.next();
            };
            statement.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println("Can't connect to database!" + e.getMessage());
        }
    }

    public void addActionListener(ActionListener actionListener) {
        logoutButton.addActionListener(actionListener);
        tableSelector.addActionListener(actionListener);
    }

    public void setTable() {
        if (tableSelector.getSelectedItem() != null) {
            String tableName = tableSelector.getSelectedItem().toString();
            try {
                connect = databaseConnect.getConnection();
                Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                String query = "SELECT * FROM " + tableName;

                ResultSet results = statement.executeQuery(query);

                int colsCount = results.getMetaData().getColumnCount();
                results.last();
                int rowsCount = results.getRow();
                results.first();

                String[] colsNames = new String[colsCount];
                Object[][] data = new Object[rowsCount][colsCount];

                for (int i = 0; i < colsCount; i++) {
                    colsNames[i] = results.getMetaData().getColumnName(i+1);
                    results.first();
                    for (int j = 0; j<rowsCount; j++) {
                        data[j][i] = results.getObject(colsNames[i]);
                        results.next();
                    }
                }
                statement.close();
                connect.close();

                tableView = new JTable(data, colsNames);
                tableView.setGridColor(Color.BLACK);
                tableView.setFillsViewportHeight(true);
                tableView.setEnabled(false);

                JTableHeader tableHeader = tableView.getTableHeader();

                scrollPane.setColumnHeaderView(tableHeader);
                scrollPane.setViewportView(tableView);
            } catch (Throwable e) {
                throw new Error("Problem at setTable: ", e);
            }
        }
    }
}
