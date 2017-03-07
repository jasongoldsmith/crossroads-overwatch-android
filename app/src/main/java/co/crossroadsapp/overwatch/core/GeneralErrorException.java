package co.crossroadsapp.overwatch.core;

import co.crossroadsapp.overwatch.data.GeneralServerError;

/**
 * Created by sharmha on 2/23/17.
 */

public class GeneralErrorException extends OverwatchLoginException {
    public GeneralErrorException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public GeneralErrorException(int errorCode, String msg, Object customData) {
        super(errorCode, msg, customData);
    }

    public GeneralErrorException(int errorCode, Exception inner) {
        super(errorCode, inner);
    }

    public GeneralErrorException(int errorCode) {
        super(errorCode);
    }
}
