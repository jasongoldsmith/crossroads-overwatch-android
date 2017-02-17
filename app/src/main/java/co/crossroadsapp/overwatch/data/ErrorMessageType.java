package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karagdi on 2/2/17.
 */

public class ErrorMessageType {
    private String _type = null;

    public String getType()
    {
        return this._type;
    }

    public void toJson(JSONObject json) throws JSONException {
        if (json.has("type") && !json.isNull("type")) {
            if(json.get("type") instanceof String) {
                this._type = json.getString("title");
            }
        }
    }
}
