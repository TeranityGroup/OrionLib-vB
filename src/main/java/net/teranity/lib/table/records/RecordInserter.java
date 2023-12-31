package net.teranity.lib.table.records;

import lombok.*;
import net.teranity.lib.OrionTable;
import net.teranity.lib.exceptions.RecordException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RequiredArgsConstructor(staticName = "build")
public class RecordInserter {
    @NonNull @Getter private OrionTable orionTable;
    @Getter private ArrayList<String> records = new ArrayList<>();
    @Getter private ArrayList<Object> objects = new ArrayList<>();

    @Getter private String query;

    private boolean next;

    @SneakyThrows(SQLException.class)
    public boolean alreadyExists() {
        String sql = "select * from " + getOrionTable().getTableName() + " where " + getRecords().get(1) + " = ?";
        PreparedStatement statement = getOrionTable().getConnection().prepareStatement(sql);
        statement.setObject(1, getObjects().get(1));

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return true;
        } return false;
    }

    public boolean isMatches() {
        return (getRecords().size() == getObjects().size());
    }

    public void setup() throws RecordException {
        try {
            StringBuilder recordBuilder = new StringBuilder();
            StringBuilder objectStrBuilder = new StringBuilder();

            int length;

            for (@NonNull String record : getRecords()) {
                recordBuilder.append(record + ", ");
            }
            length = recordBuilder.length();
            length = length - 2;
            recordBuilder.deleteCharAt(length);

            length = 0;

            for (@NonNull Object object : getObjects()) {
                objectStrBuilder.append("?, ");
            }
            length = objectStrBuilder.length();
            length = length - 2;
            objectStrBuilder.deleteCharAt(length);

            query = "insert into " + getOrionTable().getTableName() + " (" + recordBuilder.toString() +  ") values (" + objectStrBuilder.toString() + ")";
            PreparedStatement statement = getOrionTable().getConnection().prepareStatement(query);
            for (@NonNull Object object : getObjects()) {
                int index = getObjects().indexOf(object) + 1;

                if (object instanceof String) {
                    statement.setString(index, (String) object);
                }else if (object instanceof Integer) {
                    statement.setInt(index, (Integer) object);
                }else if (object instanceof Boolean) {
                    statement.setBoolean(index, (Boolean) object);
                }
            }

            statement.executeUpdate();
            next = true;
        }catch (SQLException e) {
            throw new RecordException("Database server error, records might not matches with object,");
        }
    }

    public boolean next() {
        return (next != false);
    }

    public void addRecord(String... records) {
        for (String record : records) {
            getRecords().add(record);
        }
    }

    public void addObject(Object... objects) {
        for (Object object : objects) {
            getObjects().add(object);
        }
    }
}
