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
    @Getter private String select, parent;
    @Getter private Object selectObject, parentObject;

    @Getter private Object previousObject;

    @Getter private ResultSet resultSet;
    private boolean next;

    public void setup() throws RecordException {
        checkPreviousObject();

        try {
            String sql = "update " + getOrionTable().getTableName() + " set " + select + " = ? where " + parent + " = ?";
            PreparedStatement statement = getOrionTable().getConnection().prepareStatement(sql);
            statement.setObject(1, selectObject);
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
            String sql = "select " + selectObject + " from " + getOrionTable().getTableName() + " where " + parent + " = ?";
            PreparedStatement statement = getOrionTable().getConnection().prepareStatement(sql);
            statement.setObject(1, parentObject);

            ResultSet resultSet1 = statement.executeQuery();
            while (resultSet1.next()) {
                previousObject = resultSet1.getObject(select);
            }
        }catch (SQLException e) {
            throw new RecordException("Database server error, no record found of " + select);
        }
    }

    public boolean next() {
        return (next != false);
    }

    public void setSelect(String select, Object selectObject) {
        this.select = select;
        this.selectObject = selectObject;
    }

    public void setParent(String parent, Object parentObject) {
        this.parent = parent;
        this.parentObject = parentObject;
    }
}
