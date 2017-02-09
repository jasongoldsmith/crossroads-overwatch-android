package co.crossroadsapp.overwatch.core;

/**
 * Created by karagdi on 2/2/17.
 */

public class AddConsoleException extends OverwatchLoginException {
    public AddConsoleException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public AddConsoleException(int errorCode, String msg, Object customData) {
        super(errorCode, msg, customData);
    }

    public AddConsoleException(int errorCode, Exception inner) {
        super(errorCode, inner);
    }

    public AddConsoleException(int errorCode) {
        super(errorCode);
    }
}
