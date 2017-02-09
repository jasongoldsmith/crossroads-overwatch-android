package co.crossroadsapp.overwatch.core;

/**
 * Created by karagdi on 2/2/17.
 */

public class OverwatchLoginException extends TrimbleException {
    private String _userTag;
    public String getUserTag()
    {
        return this._userTag;
    }

    public void setUserTag( String userTag )
    {
        this._userTag = userTag;
    }

    public OverwatchLoginException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public OverwatchLoginException(int errorCode, String msg, Object customData) {
        super(errorCode, msg, customData);
    }

    public OverwatchLoginException(int errorCode, Exception inner) {
        super(errorCode, inner);
    }

    public OverwatchLoginException(int errorCode) {
        super(errorCode);
    }
}
