package co.crossroadsapp.overwatch.data;

import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shaded.fasterxml.jackson.annotation.JsonInclude;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;
import com.shaded.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 8/3/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "privacyVersion",
        "termsVersion",
        "termsNeedsUpdate",
        "privacyNeedsUpdate"
})
public class LegalData {
    @JsonProperty("privacyVersion")
    private String privacyVersion = null;
    @JsonProperty("termsVersion")
    private String termsVersion = null;
    @JsonProperty("termsNeedsUpdate")
    private boolean termsNeedsUpdate = false;
    @JsonProperty("privacyNeedsUpdate")
    private boolean privacyNeedsUpdate = false;

    @JsonProperty("privacyVersion")
    public void setPrivacyVersion(String pver) {
        this.privacyVersion = pver;
    }

    @JsonProperty("privacyVersion")
    public String getPrivacyVersion() {
        return privacyVersion;
    }

    @JsonProperty("termsVersion")
    public void setTermsVersion(String tver) {
        this.termsVersion = tver;
    }

    @JsonProperty("termsVersion")
    public String getTermsVersion() {
        return termsVersion;
    }

    @JsonProperty("termsNeedsUpdate")
    public void setTermsNeedsUpdate(boolean tUpdate) {
        this.termsNeedsUpdate = tUpdate;
    }

    @JsonProperty("privacyNeedsUpdate")
    public boolean getTermsNeedsUpdate() {
        return termsNeedsUpdate;
    }

    @JsonProperty("privacyNeedsUpdate")
    public void setPrivacyNeedsUpdate(boolean pUpdate) {
        this.privacyNeedsUpdate = pUpdate;
    }

    @JsonProperty("termsNeedsUpdate")
    public boolean getPrivacyNeedsUpdate() {
        return privacyNeedsUpdate;
    }

    public void toJson(JSONObject json) {
        try {
            if(json!=null) {
                if(json.has("privacyVersion") && !json.isNull("privacyVersion")) {
                    setPrivacyVersion(json.getString("privacyVersion"));
                }
                if(json.has("termsVersion") && !json.isNull("termsVersion")) {
                    setTermsVersion(json.getString("termsVersion"));
                }
                if(json.has("termsNeedsUpdate") && !json.isNull("termsNeedsUpdate")) {
                    setTermsNeedsUpdate(json.getBoolean("termsNeedsUpdate"));
                }
                if(json.has("privacyNeedsUpdate") && !json.isNull("privacyNeedsUpdate")) {
                    setPrivacyNeedsUpdate(json.getBoolean("privacyNeedsUpdate"));
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
