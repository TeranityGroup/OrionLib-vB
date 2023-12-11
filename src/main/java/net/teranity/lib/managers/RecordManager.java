package net.teranity.lib.managers;

import lombok.Getter;
import lombok.Setter;
import net.teranity.lib.OrionLib;
import net.teranity.lib.OrionTable;
import net.teranity.lib.exceptions.RecordException;

import java.sql.Connection;

public abstract class RecordManager {

    @Getter private OrionLib orionLib = OrionLib.getInstance();
    @Getter private OrionTable orionTable = orionLib.getOrionTable();
    @Getter @Setter private Connection connection = orionTable.getConnection();

    public abstract void setup() throws RecordException;

    public abstract boolean next();

    public void run() throws RecordException {
        setup();
    }
}
