package net.teranity.lib.table.records;

import lombok.*;
import net.teranity.lib.OrionTable;
import net.teranity.lib.exceptions.RecordException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "build")
public class RecordSetter {
    @NonNull @Getter private OrionTable orionTable;
    @Getter @Setter private String setRecord, parentRecord;
    @Getter @Setter private Object setObject, parentObject;

    @Getter private Object previousObject;

    @Getter private ResultSet resultSet;
    private boolean next;

    public void setup() throws RecordException {
        checkPreviousObject();

        try {
            String sql = "update " + getOrionTable().getTableName() + " set " + setRecord + " = ? where " + parentRecord + " = ?";
            PreparedStatement statement = getOrionTable().getConnection().prepareStatement(sql);
            statement.setObject(1, setObject);
            statement.setObject(2, parentObject);
            statement.executeUpdate();

            resultSet = statement.executeQuery();
            next = true;
        }catch (SQLException e) {
            throw new RecordException("Database server error, unknown reason");
        }
    }

    private void checkPreviousObject() throws RecordException {
        try {
            String sql = "select " + setRecord + " from " + getOrionTable().getTableName() + " where " + parentRecord + " = ?";
            PreparedStatement statement = getOrionTable().getConnection().prepareStatement(sql);
            statement.setObject(1, parentObject);

            ResultSet resultSet1 = statement.executeQuery();
            while (resultSet1.next()) {
                previousObject = resultSet1.getObject(setRecord);
            }
        }catch (SQLException e) {
            throw new RecordException("Database server error, no record found of " + setRecord);
        }
    }

    public boolean next() {
        return (next != false);
    }
}
