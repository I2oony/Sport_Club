package main;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.*;

public class DatabaseConnect {
    private final String host;
    private final int port;
    private final String database;
    private String username;
    private String password;

    private DataSource source;

    public DatabaseConnect(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (this.source == null) {
            MysqlDataSource dbConnect = new MysqlDataSource();

            dbConnect.setServerName(this.host);
            dbConnect.setPort(this.port);
            dbConnect.setDatabaseName(this.database);
            dbConnect.setUser(this.username);
            dbConnect.setPassword(this.password);

            dbConnect.setCharacterEncoding("UTF-8");
            dbConnect.setServerTimezone("UTC");

            this.source = dbConnect;
        }
        return this.source.getConnection();
    }


    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
}
