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
public class RecordGetter {
    @NonNull @Getter private OrionTable orionTable;
    @Getter private ResultSet resultSet;

    private boolean next;

    @Getter @Setter private String select;
    @Getter private String parent;
    @Getter private Object parentObject;

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

    public void setup() throws RecordException {
        try {
            String sql;

            if (parent == null && parentObject == null) {
                sql = "select " + select + " from " + getOrionTable().getTableName();
            }else {
                sql = "select " + select + " from " + getOrionTable().getTableName() + " where " + parent + " = ?";
            }

            PreparedStatement statement = getOrionTable().getConnection().prepareStatement(sql);
            if (parentObject != null) {
                statement.setObject(1, parentObject);
            }

            resultSet = statement.executeQuery();
            next = true;
        }catch (SQLException e) {
            throw new RecordException("Database Server error, no record found of " + select);
        }
    }

    public boolean next() {
        return (next != false);
    }

    public void setParent(String parent, Object parentObject) {
        this.parent = parent;
        this.parentObject = parentObject;
    }
}
