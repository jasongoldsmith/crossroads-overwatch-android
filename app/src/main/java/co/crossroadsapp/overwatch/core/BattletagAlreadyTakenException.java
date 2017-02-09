package co.crossroadsapp.overwatch.core;

/**
 * Created by karagdi on 1/26/17.
 */

public class BattletagAlreadyTakenException extends OverwatchLoginException {
    public BattletagAlreadyTakenException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public BattletagAlreadyTakenException(int errorCode, String msg, Object customData) {
        super(errorCode, msg, customData);
    }

    public BattletagAlreadyTakenException(int errorCode, Exception inner) {
        super(errorCode, inner);
    }

    public BattletagAlreadyTakenException(int errorCode) {
        super(errorCode);
    }
}
