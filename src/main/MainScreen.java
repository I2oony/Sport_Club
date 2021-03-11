package main;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EventObject;

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
    private JLabel statusTitle;
    private JPanel statusPanel;
    private JPanel metricsPanel;
    private JLabel stateTitle;
    private JLabel stateValue;
    private JLabel hostTitle;
    private JLabel hostValue;
    private JLabel portTitle;
    private JLabel portValue;
    private JPanel editingDataPanel;
    private JButton editRowButton;
    private JButton addRowButton;
    private JButton deleteRowButton;

    Role role;
    DatabaseConnect databaseConnect;
    Connection connect;

    public JPanel getScreen(){
        return rootPanel;
    }

    public void setRole(Role r){
        role = r;
        if (r.equals(Role.SPORTSMAN)) {
            editingDataPanel.setVisible(false);
            userRole.setText("Спортсмен");
        } else {
            editingDataPanel.setVisible(true);
            userRole.setText("Тренер");
        }
    }

    public void setDatabaseConnect (DatabaseConnect databaseConnect) {
        try {
            this.databaseConnect = databaseConnect;
            connect = this.databaseConnect.getConnection();

            Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "show tables";

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
            updateServerState("OK");
        } catch (SQLException e) {
            updateServerState("Can't connect to database!");
        }
    }

    public void addActionListener(ActionListener actionListener) {
        logoutButton.addActionListener(actionListener);
        tableSelector.addActionListener(actionListener);

        editRowButton.addActionListener(actionListener);
        addRowButton.addActionListener(actionListener);
        deleteRowButton.addActionListener(actionListener);
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

                tableView = new JTable(data, colsNames) {
                    @Override
                    public boolean editCellAt(int row, int column, EventObject e) {
                        return false;
                    }
                };
                tableView.setGridColor(Color.BLACK);
                tableView.setFillsViewportHeight(true);

                if (role.equals(Role.SPORTSMAN)) {
                    tableView.setEnabled(false);
                } else {
                    tableView.setEnabled(true);
                }

                tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                JTableHeader tableHeader = tableView.getTableHeader();
                tableHeader.setReorderingAllowed(false);

                scrollPane.setColumnHeaderView(tableHeader);
                scrollPane.setViewportView(tableView);

                updateServerState("OK");
            } catch (Throwable e) {
                updateServerState("Can't fetch selected table");
            }
        }
    }

    public void clearData() {
        scrollPane.setColumnHeaderView(null);
        scrollPane.setViewportView(null);
        tableSelector.removeAllItems();
    }

    public void updateServerState(String state) {
        if (state.equals("OK")) {
            stateValue.setText("Подключен");
            stateValue.setForeground(Colors.O_GREEN);
            hostValue.setText(databaseConnect.getHost());
            portValue.setText(String.valueOf(databaseConnect.getPort()));
        } else {
            stateValue.setText("Ошибка подключения: " + state);
            stateValue.setForeground(Colors.O_RED);
            hostValue.setText(null);
            portValue.setText(null);
        }
    }

    public int getSelectedRowId() {
        int id = 0;
        try {
            int rowIndex = tableView.getSelectedRow();
            System.out.println("Row: " + rowIndex);
            id = (int) tableView.getValueAt(rowIndex, 0);
            System.out.println("Selected: " + id);
            return id;
        } catch (Throwable e) {
            System.out.println("Something went wrong...");
            return -1;
        }
    }

    public String getCurrentTable() {
        return tableSelector.getSelectedItem().toString();
    }

    public boolean deleteRow(int id) {
        String table = tableSelector.getSelectedItem().toString();
        try {
            connect = this.databaseConnect.getConnection();

            Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "DELETE from " + table + " where id = " + "'" + id + "'";

            int result = statement.executeUpdate(query);

            if (result!=0) {
                System.out.println("Deleted!");
            } else {
                throw new Throwable();
            }

            setTable();

            statement.close();
            connect.close();

            return true;
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case 1054: //1054 - can't found id = can't delete from this table
                    System.out.println("You can't delete from this table!");
                    break;
                case 1451: //1451 - can't delete - this entity exists in another table
                    System.out.println("The deletion of the selected entity is restricted due to related data consistent policy!");
                    break;
                default:
                    System.out.println("Unknown SQL Exception!\n" + e.getErrorCode() + " - " + e.getMessage());
                    break;
            }
            return false;
        } catch (Throwable e) {
            System.out.println("Something went wrong...");
            return false;
        }
    }
}
