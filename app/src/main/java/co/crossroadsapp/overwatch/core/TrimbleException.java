package co.crossroadsapp.overwatch.core;

/**
 * Created by karagdi on 1/27/16.
 */
public class TrimbleException extends Exception {
    private int _errorCode;
    private Object _customData;
    private Exception _innerException;

    public static final int TRIMBLE_BAD_REQUEST = 400;
    public static final int TRIMBLE_USER_NOT_AUTHENTICATED = 401;

    public TrimbleException(final int errorCode, String msg )
    {
        super( msg );
        this._errorCode = errorCode;
    }

    public TrimbleException(final int errorCode, String msg, Object customData )
    {
        super( msg );
        this._errorCode = errorCode;
        this._customData = customData;
    }

    public TrimbleException(final int errorCode, Exception inner )
    {
        this( errorCode );
        this._innerException = inner;
    }


    public TrimbleException(final int errorCode )
    {
        this( errorCode, "Error Code: " + errorCode );
    }

    public int getErrorCode()
    {
        return this._errorCode;
    }
    public Object getCustomData()
    {
        return this._customData;
    }

    public void printStackTrace()
    {
        super.printStackTrace();
        if( this._innerException != null )
        {
            this._innerException.printStackTrace();
        }
    }
}