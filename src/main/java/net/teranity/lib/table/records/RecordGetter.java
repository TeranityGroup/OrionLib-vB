package net.teranity.lib.table.records;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.managers.RecordManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor(staticName = "build")
public class RecordGetter extends RecordManager {
    @Getter private ResultSet resultSet;

    private boolean next;

    @Getter @Setter private String select;
    @Getter @Setter private String parent;
    @Getter @Setter private Object parentObject;

    @NonNull
    public Object get() throws RecordException {
        try {
            while (resultSet.next()) {
                return resultSet.getObject(getSelect());
            }
        }catch (SQLException e) {
            throw new RecordException("");
        } return null;
    }

    @Override
    public void setup() throws RecordException {
        try {
            String sql;

            if (parent == null && parentObject == null) {
                sql = "select " + select + " from " + getOrionTable().getTableName();
            }else {
                sql = "select " + select + " from " + getOrionTable().getTableName() + " where " + parent + " = ?";
            }

            PreparedStatement statement = getConnection().prepareStatement(sql);
            if (parentObject != null) {
                statement.setObject(1, parentObject);
            }

            resultSet = statement.executeQuery();
            next = true;
        }catch (SQLException e) {
            throw new RecordException("Database Server error, no record found of " + select);
        }
    }

    @Override
    public boolean next() {
        return (next != false);
    }
}
