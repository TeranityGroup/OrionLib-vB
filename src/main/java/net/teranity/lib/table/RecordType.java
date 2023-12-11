package net.teranity.lib.table;

import javax.print.DocFlavor;

public enum RecordType {
    STRING("varchar"),
    INT("int"),
    BOOLEAN("boolean");

    private String a;

    RecordType(String a) {
        this.a = a;
    }

    public static RecordType get(String a) {
        for (RecordType recordType : values())
            if (recordType.getA() == a)
                return recordType;
        return null;
    }

    public String getA() {
        return a;
    }
}
