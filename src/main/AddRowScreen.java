package main;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddRowScreen {
    private JPanel rootPanel;
    private JButton closeButton;
    private JLabel title;
    private JButton addToBaseButton;
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel value1;
    private JLabel value2;
    private JLabel value3;
    private JTextField textField3;
    private JLabel value4;
    private JTextField textField4;
    private JLabel value5;
    private JTextField textField5;
    private JLabel value6;
    private JTextField textField6;
    private JLabel value7;
    private JTextField textField7;
    private JLabel errorMessage;
    private JLabel value8;
    private JTextField textField8;
    private JLabel nextIdTitle;

    private JLabel[] titles = {value1, value2, value3, value4, value5, value6, value7, value8};
    private String[] values = new String[8];
    private JTextField[] fields = { textField1, textField2, textField3, textField4,
                                    textField5, textField6, textField7, textField8};

    DatabaseConnect databaseConnect;
    Connection connect;
    String currentTable;
    int rows;
    int newId;

    public JPanel getScreen(String table, DatabaseConnect databaseConnect) {
        currentTable = table;
        title.setText("Добавление данных в табличку: " + currentTable);
        mainPanel.setVisible(false);
        errorMessage.setVisible(false);
        try {
            this.databaseConnect = databaseConnect;
            connect = this.databaseConnect.getConnection();

            Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "select count(privilege_type) from information_schema.table_privileges\n" +
                    "where table_privileges.table_name = \"" + currentTable + "\"\n" +
                    "and table_privileges.grantee like \"%trainer%\"\n" +
                    "and privilege_type = 'insert'";

            ResultSet results = statement.executeQuery(query);
            results.next();

            int isInsertAllowed = results.getInt(1);
            System.out.println(isInsertAllowed);

            if (isInsertAllowed==1) {
                mainPanel.setVisible(true);
                query = "show full columns from " + currentTable;
                results = statement.executeQuery(query);

                results.last();
                rows = results.getRow() - 1;
                results.first();

                for (int i = 0; i < rows; i++) {
                    results.next();
                    String column = results.getString(1);
                    values[i] = column;
                    String comment = results.getString(9);
                    titles[i].setText(comment);
                    titles[i].setVisible(true);
                    fields[i].setVisible(true);
                }

                System.out.println("found max...");

                query = "select max(id) from " + currentTable;
                results = statement.executeQuery(query);
                results.next();

                newId = results.getInt(1) + 1;
                System.out.println("New id: " + newId);

                nextIdTitle.setText("Идентификатор добавляемой записи: " + newId);

            } else {
                throw new Throwable();
            }
        } catch (SQLException sqlException) {
            errorMessage.setText("SQLException: " + sqlException.getErrorCode() + ": " + sqlException.getMessage());
            errorMessage.setVisible(true);
        } catch (Throwable e) {
            errorMessage.setText("Can't get access to the selected table.\n" + e.getMessage());
            errorMessage.setVisible(true);
        }
        return rootPanel;
    }

    public void addActionListener(ActionListener actionListener) {
        closeButton.addActionListener(actionListener);
        addToBaseButton.addActionListener(actionListener);
    }

    public void clearAll() {
        for (int i = 0; i<8; i++) {
            titles[i].setVisible(false);
            fields[i].setVisible(false);
        }
    }

    public void addRow() {
        String query = "insert into " + currentTable + " (id";
        for (int i = 0; i<rows; i++) {
            query = query.concat(", " + values[i]);
        }
        query = query.concat(") values ('" + newId);
        for (int i = 0; i<rows; i++) {
            if (fields[i].getText().length() == 0) {
                fields[i].setText(" ");
            }
            query = query.concat("', '" + fields[i].getText());
        }
        query = query.concat("')");

        System.out.println(query);

        try {
            Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int result = statement.executeUpdate(query);

            System.out.println("added: " + result);

        } catch (SQLException sqlException) {
            errorMessage.setText("SQLException: " + sqlException.getErrorCode() + ": " + sqlException.getMessage());
            errorMessage.setVisible(true);
        }
    }

}
