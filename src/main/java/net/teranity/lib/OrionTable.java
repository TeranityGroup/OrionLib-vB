package net.teranity.lib;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Getter
public class OrionTable {
    private String tableName;
    @NonNull private Connection connection;

    public OrionTable(@NonNull String tableName, @NonNull Connection connection) {
        this.tableName = tableName;
        this.connection = connection;

        if (!exists()) return;
    }

    @SneakyThrows(SQLException.class)
    public boolean exists(String tableName) {
        String sql = "select * from " + getTableName();
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return true;
        } return false;
    }

    public boolean exists() {
        return (exists(getTableName()) != false);
    }
}
