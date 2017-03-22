
package co.crossroadsapp.overwatch.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnBoardingScreens {

    @SerializedName("required")
    @Expose
    private List<Required> required = null;
    @SerializedName("optional")
    @Expose
    private List<Object> optional = null;

    public List<Required> getRequired() {
        return required;
    }

    public void setRequired(List<Required> required) {
        this.required = required;
    }

    public List<Object> getOptional() {
        return optional;
    }

    public void setOptional(List<Object> optional) {
        this.optional = optional;
    }

    public void parseonbaordingData(JSONObject data) {
        try {
            if(data.has("required") && !data.isNull("required")) {
                JSONArray req = data.getJSONArray("required");
                if(req!=null && req.length()>0) {
                    required = new ArrayList<>();
                    for(int i=0;i<req.length();i++) {
                        if(req.getJSONObject(i)!=null) {
                            Required required1 = new Required();
                            JSONObject temp = req.getJSONObject(i);
                            required1.parse(temp);
                            required.add(required1);
                        }
                    }
                    setRequired(required);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
