package net.teranity.lib.table.records;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.teranity.lib.OrionTable;
import net.teranity.lib.exceptions.RecordException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "build")
public class RecordRemover {
    @NonNull @Getter private OrionTable orionTable;
    @Getter private String parent;
    @Getter private Object parentObject;

    @Getter private String secondParent;
    @Getter private Object secondParentObject;

    @Getter private ResultSet resultSet;

    private boolean next;

    public void setup() throws RecordException {
        try {
            String sql;
            PreparedStatement statement;

            if (secondParent == null) {
                sql = "delete from " + orionTable.getTableName() + " where " + parent + " = ?";
                statement = orionTable.getConnection().prepareStatement(sql);
                statement.setObject(1, parentObject);
            }else {
                sql = "delete from " + orionTable.getTableName() + " where " + secondParent + " = ? and " + parent + " = ?";
                statement = orionTable.getConnection().prepareStatement(sql);
                statement.setObject(1, secondParentObject);
                statement.setObject(2, parentObject);
            }

            statement.executeUpdate();

            resultSet = statement.executeQuery();
            next = true;
        }catch (SQLException e) {
            throw new RecordException("");
        }
    }

    public void setParent(String parent, Object parentObject) {
        this.parent = parent;
        this.parentObject = parentObject;
    }

    public void setSecondParent(String secondParent, Object secondParentObject) {
        this.secondParent = secondParent;
        this.secondParentObject = secondParentObject;
    }

    public boolean next() {
        return (next != false);
    }
}
