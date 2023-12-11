package net.teranity.lib.exceptions;

public class NullConnectionException extends Throwable {

    public NullConnectionException(String f, String reason) {
        super(f + ", " + reason);
    }
}
