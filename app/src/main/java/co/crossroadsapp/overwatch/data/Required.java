
package co.crossroadsapp.overwatch.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Required {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("backgroundImageUrl")
    @Expose
    private String backgroundImageUrl;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("heroImageUrl")
    @Expose
    private String heroImageUrl;
    @SerializedName("textImageUrl")
    @Expose
    private String textImageUrl;
    @SerializedName("isRequired")
    @Expose
    private Boolean isRequired;
    @SerializedName("language")
    @Expose
    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getHeroImageUrl() {
        return heroImageUrl;
    }

    public void setHeroImageUrl(String heroImageUrl) {
        this.heroImageUrl = heroImageUrl;
    }

    public String getTextImageUrl() {
        return textImageUrl;
    }

    public void setTextImageUrl(String textImageUrl) {
        this.textImageUrl = textImageUrl;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void parse(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                if (jsonObject.has("_id") && !jsonObject.isNull("_id")) {
                    if (!jsonObject.getString("_id").isEmpty()) {
                        setId(jsonObject.getString("_id"));
                    }
                }
                if (jsonObject.has("backgroundImageUrl") && !jsonObject.isNull("backgroundImageUrl")) {
                    if (!jsonObject.getString("backgroundImageUrl").isEmpty()) {
                        setBackgroundImageUrl(jsonObject.getString("backgroundImageUrl"));
                    }
                }
                if (jsonObject.has("heroImageUrl") && !jsonObject.isNull("heroImageUrl")) {
                    if (!jsonObject.getString("heroImageUrl").isEmpty()) {
                        setHeroImageUrl(jsonObject.getString("heroImageUrl"));
                    }
                }
                if (jsonObject.has("textImageUrl") && !jsonObject.isNull("textImageUrl")) {
                    if (!jsonObject.getString("textImageUrl").isEmpty()) {
                        setTextImageUrl(jsonObject.getString("textImageUrl"));
                    }
                }
                if (jsonObject.has("language") && !jsonObject.isNull("language")) {
                    if (!jsonObject.getString("language").isEmpty()) {
                        setLanguage(jsonObject.getString("language"));
                    }
                }
                if (jsonObject.has("order") && !jsonObject.isNull("order")) {
                    setOrder(jsonObject.getInt("order"));
                }
                if (jsonObject.has("isRequired") && !jsonObject.isNull("isRequired")) {
                    setIsRequired(jsonObject.getBoolean("isRequired"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
