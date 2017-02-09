package co.crossroadsapp.overwatch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karagdi on 2/2/17.
 */

public class GeneralServerError implements Parcelable {
    private String _type = null;
    private int _code = -1;
    private ErrorDetails _details;

    public static final int INVALID_MAIL_PROVIDED = 2;
    public static final int NO_USER_FOUND_WITH_THE_EMAIL = 5;
    public static final int ALREADY_TAKEN = 11;

    public GeneralServerError(){
    }

    protected GeneralServerError(Parcel in) {
        _type = in.readString();
        _code = in.readInt();
        _details = in.readParcelable(ErrorDetails.class.getClassLoader());
    }

    public static final Creator<GeneralServerError> CREATOR = new Creator<GeneralServerError>() {
        @Override
        public GeneralServerError createFromParcel(Parcel in) {
            return new GeneralServerError(in);
        }

        @Override
        public GeneralServerError[] newArray(int size) {
            return new GeneralServerError[size];
        }
    };

    public String getType()
    {
        return this._type;
    }

    public int getCode()
    {
        return this._code;
    }

    public ErrorDetails getErrorDetails()
    {
        return this._details;
    }

    public void toJson(JSONObject json) throws JSONException {
        if (json.has("type") && !json.isNull("type")) {
            this._type = json.getString("type");
        }
        if (json.has("code") && !json.isNull("code")) {
            this._code = json.getInt("code");
        }
        if( json.has("details") && !json.isNull("details")) {
            this._details = new ErrorDetails();
            this._details.toJson(json.optJSONObject("details"));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_type);
        dest.writeInt(_code);
        dest.writeParcelable(_details, flags);
    }
}
