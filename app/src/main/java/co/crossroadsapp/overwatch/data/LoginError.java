package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karagdi on 1/26/17.
 */
public class LoginError {
    private GeneralServerError _error;
    private String _description = null;
    private String _responseType;
    private ErrorMessageType _messageType;

    public String getDescription()
    {
        return this._description;
    }

    public String getResponseType()
    {
        return this._responseType;
    }

    public GeneralServerError getGeneralServerError()
    {
        return this._error;
    }

    public void toJson(JSONObject json) {
        try {
            if (json.has("error") && !json.isNull("error")) {
                JSONObject jsonError = json.optJSONObject("error");
                if (jsonError != null) {
                    this._error = new GeneralServerError();
                    this._error.toJson(jsonError);
                    if (jsonError.has("description") && !jsonError.isNull("description")) {
                        this._description = jsonError.getString("description");
                    }
                }
            }
            if(this._error!=null && _error.getCode()>=0) {
                if (json.has("message") && !json.isNull("message")) {
                    if (json.get("message") instanceof Integer) {
                        int message = json.getInt("message");
                    }
                }
                if (json.has("responseType") && !json.isNull("responseType")) {
                    this._responseType = json.getString("responseType");
                }
                if (json.has("message") && !json.isNull("message")) {
                    JSONObject jsonError = json.optJSONObject("message");
                    this._messageType = new ErrorMessageType();
                    this._messageType.toJson(jsonError);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}