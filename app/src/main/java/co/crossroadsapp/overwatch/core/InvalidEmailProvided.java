package co.crossroadsapp.overwatch.core;

/**
 * Created by karagdi on 2/2/17.
 */

public class InvalidEmailProvided extends OverwatchLoginException {
    public InvalidEmailProvided(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public InvalidEmailProvided(int errorCode, String msg, Object customData) {
        super(errorCode, msg, customData);
    }

    public InvalidEmailProvided(int errorCode, Exception inner) {
        super(errorCode, inner);
    }

    public InvalidEmailProvided(int errorCode) {
        super(errorCode);
    }
}
