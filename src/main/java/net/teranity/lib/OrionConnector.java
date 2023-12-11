package net.teranity.lib;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "get")
public class OrionConnector {

    @NonNull @Getter private String username, password, database, host;
    @NonNull @Getter private int port;

    @NonNull private boolean useSSL;

    @Getter private HikariDataSource hikari;
    @Getter private Connection connection;
    private boolean next;

    public void connect() throws SQLException {
        if (next == true)
            if (connection != null) return;
        next = false;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("useSSL", useSSL);

        hikari = new HikariDataSource(hikariConfig);
        connection = getHikari().getConnection();
        next = true;
    }
}
