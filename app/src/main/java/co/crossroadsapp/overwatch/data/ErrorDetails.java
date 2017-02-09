package co.crossroadsapp.overwatch.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karagdi on 2/2/17.
 */
public class ErrorDetails implements Parcelable {
    private String _title;
    private String _message;
    private String _comments;

    public ErrorDetails(){
    }

    protected ErrorDetails(Parcel in) {
        this._title = in.readString();
        this._message = in.readString();
        this._comments = in.readString();
    }

    public static final Creator<ErrorDetails> CREATOR = new Creator<ErrorDetails>() {
        @Override
        public ErrorDetails createFromParcel(Parcel in) {
            return new ErrorDetails(in);
        }

        @Override
        public ErrorDetails[] newArray(int size) {
            return new ErrorDetails[size];
        }
    };

    public String getTitle()
    {
        return this._title;
    }

    public String getMessage()
    {
        return this._message;
    }

    public String getComments()
    {
        return this._comments;
    }

    public void toJson(JSONObject json) throws JSONException {
        if (json.has("title") && !json.isNull("title")) {
            this._title = json.getString("title");
        }
        if (json.has("message") && !json.isNull("message")) {
            this._message = json.getString("message");
        }
        if (json.has("comments") && !json.isNull("comments")) {
            this._comments = json.getString("comments");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_title);
        dest.writeString(_message);
        dest.writeString(_comments);
    }
}
