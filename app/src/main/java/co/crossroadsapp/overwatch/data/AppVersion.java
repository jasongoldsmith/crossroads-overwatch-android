package co.crossroadsapp.overwatch.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 3/18/16.
 */
public class AppVersion {

    private String version;

    public void setVersion(String ver) {
        version = ver;
    }

    public String getVersion() {
        return this.version;
    }

    public void toJson(JSONObject json) {
        try {

            String n = json.getString("latestAndroidVersion");
            if (n!=null && !n.isEmpty()){
                setVersion(n);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
