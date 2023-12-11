package net.teranity.lib;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.teranity.lib.exceptions.NullConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public class OrionLib {
    @Getter private static OrionLib instance;

    @Getter private OrionConnector orionConnector;
    @Getter private Connection connection;

    @Getter @Setter private OrionTable orionTable;

    @NonNull
    public Connection connector(@NonNull OrionConnector orionConnector) throws NullConnectionException{
        try {
            orionConnector.connect();

            connection = orionConnector.getConnection();
            return connection;
        }catch (SQLException e) {
            throw new NullConnectionException("No connection for: " + orionConnector.getHost() + ":" + orionConnector.getPort() + "/" + orionConnector.getDatabase(), "this may caused by no running connection.");
        }
    }
}
