package co.crossroadsapp.overwatch.data;

import co.crossroadsapp.overwatch.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 2/28/16.
 */
public class PlayerData {

    private String playerId;
    private String username;
    private String psnId;
    private String playerImageUrl;
    private String playerClanTag;
    private boolean maxReported = false;
    private int commentsReported;
    private String invitedBy=null;
    private String userId=null;
    private boolean isActive = false;
    private boolean isInvited = false;

    public void setPlayerId(String id) {
        playerId = id;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setInvitedBy(String eId) {
        invitedBy = eId;
    }

    public String getInvitedBy() {
        return this.invitedBy;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPsnId(String psnid) {
        psnId = psnid;
    }

    public String getPsnId() {
        return this.psnId;
    }

    public void setPlayerImageUrl(String image) {
        playerImageUrl = image;
    }

    public String getPlayerImageUrl() {
        return this.playerImageUrl;
    }

    public void setClanTag(String clanT) {
        playerClanTag = clanT;
    }

    public String getClanTag() {
        return playerClanTag;
    }

    public void setCommentsReported(int num) {
        commentsReported = num;
    }

    public int getCommentsReported() {
        return  this.commentsReported;
    }

    public void setMaxReported(boolean maxReport) {
        maxReported = maxReport;
    }

    public boolean getMaxReported() {
        return this.maxReported;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean getActive() {
        return this.isActive;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void toJson(JSONObject jsonobject) {
        if (jsonobject != null) {
            try {
                if (jsonobject.has("consoles") && !jsonobject.isNull("consoles")) {
                    JSONArray conArray = jsonobject.optJSONArray("consoles");
//                if(conData.has("consoleType")) {
//                    String cType = conData.getString("consoleType");
//                    setConsoleType(cType);
//                }
                    if (conArray != null) {
                        for (int i = 0; i < conArray.length(); i++) {
                            JSONObject conData = (JSONObject) conArray.get(i);
                            if (conData.has("isPrimary") && !conData.isNull("isPrimary")) {
                                if (conData.getBoolean("isPrimary")) {
                                    if (conData.has("consoleId") && !conData.isNull("consoleId")) {
                                        String id = conData.getString("consoleId");
                                        setPsnId(id);
                                    }
                                    if (conData.has("clanTag") && !conData.isNull("clanTag")) {
                                        String clanTag = conData.getString("clanTag");
                                        setClanTag(clanTag);
                                    }
                                }
                            }
                        }
                    }
                }

//                if(conData.has("verifyStatus")){
//                    String verifyS = conData.getString("verifyStatus");
//                    setPsnVerify(verifyS);
//                }
//                if (!jsonobject.isNull("psnId")) {
//                    setPsnId(jsonobject.getString("psnId"));
//                }
                if (jsonobject.has("userName") && !jsonobject.isNull("userName")) {
                    setUsername(jsonobject.getString("userName"));
                }
                if (jsonobject.has("_id") && !jsonobject.isNull("_id")) {
                    setPlayerId(jsonobject.getString("_id"));
                }
                if (jsonobject.has("isActive") && !jsonobject.isNull("isActive")) {
                    setActive(jsonobject.getBoolean("isActive"));
                }
                if (jsonobject.has("invitedBy") && !jsonobject.isNull("invitedBy")) {
                    setInvitedBy(jsonobject.getString("invitedBy"));
                }
                if (jsonobject.has("_id") && !jsonobject.isNull("_id")) {
                    String uId = jsonobject.getString("_id");
                    setUserId(uId);
                }
                if (jsonobject.has("isInvited") && !jsonobject.isNull("isInvited")) {
                    boolean invited = jsonobject.getBoolean("isInvited");
                    setIsInvited(invited);
                }
                if (jsonobject.has("imageUrl") && !jsonobject.isNull("imageUrl")) {
                    setPlayerImageUrl(jsonobject.getString("imageUrl"));
                }
                if (jsonobject.has("commentsReported") && !jsonobject.isNull("commentsReported")) {
                    int num = jsonobject.getInt("commentsReported");
                    setCommentsReported(num);
                }
                if (jsonobject.has("hasReachedMaxReportedComments") && !jsonobject.isNull("hasReachedMaxReportedComments")) {
                    boolean maxRepo = jsonobject.getBoolean("hasReachedMaxReportedComments");
                    setMaxReported(maxRepo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setIsInvited(boolean isInvited) {
        this.isInvited = isInvited;
    }

    public boolean getIsInvited() {
        return isInvited;
    }
}
