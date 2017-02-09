package co.crossroadsapp.overwatch.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sharmha on 2/28/16.
 */
public class EventData {

    private String eventId=null;
    private String eventStatus=null;
    private int minPlayer=0;
    private int maxPlayer=0;
    private ActivityData activityData;
    private ArrayList<PlayerData> playerDataList;
    private ArrayList<CommentData> commentDataList;
    private CreatorData creatorData;
    private String launchDate=null;
    private String launchStatus=null;
    private String clanId=null;
    private String consoleType=null;
    private String clanName=null;
    //private PlayerData playerData;

    public EventData () {
        activityData = new ActivityData();
        playerDataList = new ArrayList<PlayerData>();
        creatorData = new CreatorData();
        commentDataList = new ArrayList<CommentData>();
    }

    public void setEventId(String id) {
        eventId = id;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setConsoleType(String console) {
        consoleType = console;
    }

    public String getConsoleType() {
        return this.consoleType;
    }

    public void setLaunchDate(String date){
        if(date!=null){
            this.launchDate = date;
        }
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchEventStatus(String status) {
        launchStatus = status;
    }

    public String getLaunchEventStatus() {
        return this.launchStatus;
    }

    public void setEventStatus(String status) {
        eventStatus = status;
    }

    public String getEventStatus() {
        return this.eventStatus;
    }

    public void setMinPlayer(int min) {
        minPlayer = min;
    }

    public int getMinPlayer() {
        return this.minPlayer;
    }

    public void setMaxPlayer(int max) {
        maxPlayer = max;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    public void setCreatorData(CreatorData creator) {
        creatorData = creator;
    }

    public CreatorData getCreatorData() {
        return this.creatorData;
    }

    public void setActivityData(ActivityData aData) {
        activityData = aData;
    }

    public ActivityData getActivityData() {
        return this.activityData;
    }

    public void setClanId(String cId) {
        clanId = cId;
    }

    public String getClanId() {
        return this.clanId;
    }

    public void setClanName(String cName) {
        clanName = cName;
    }

    public String getClanName() {
        return this.clanName;
    }

    public void setPlayerData(PlayerData pData) {
        if (playerDataList != null) {
            playerDataList.add(pData);
        }
    }

    public ArrayList<PlayerData> getPlayerData() {
        return this.playerDataList;
    }

    public ArrayList<CommentData> getCommentDataList() {
        return commentDataList;
    }

    public void toJson(JSONObject json) {
        try {
            if(json!=null) {
                if (json.has("_id")) {
                    String id = json.getString("_id");
                    setEventId(id);
                }
                if (json.has("status")) {
                    String status = json.getString("status");
                    setEventStatus(status);
                }
                if(json.has("consoleType")) {
                    String con = json.getString("consoleType");
                    setConsoleType(con);
                }
                if(json.has("clanId")) {
                    String clId = json.getString("clanId");
                    setClanId(clId);
                }
                if(json.has("clanName")) {
                    String clName = json.getString("clanName");
                    setClanName(clName);
                }
                    if (json.has("minPlayers")) {
                        int minPlayers = json.getInt("minPlayers");
                        setMinPlayer(minPlayers);
                    }
                    if (json.has("maxPlayers")) {
                        int maxPlayers = json.getInt("maxPlayers");
                        setMaxPlayer(maxPlayers);
                    }
                    if (json.has("eType")) {
                        JSONObject actData = json.optJSONObject("eType");
                        activityData.toJson(actData);
                    }
                    if (json.has("creator")) {
                        JSONObject creator = json.optJSONObject("creator");
                        creatorData.toJson(creator);
                    }
                    if (json.has("players")) {
                        JSONArray playerD = json.optJSONArray("players");
                        for (int i = 0; i < playerD.length(); i++) {
                            JSONObject jsonobject = playerD.getJSONObject(i);
                            PlayerData pData = new PlayerData();
                            pData.toJson(jsonobject);
                            playerDataList.add(pData);
                        }
                    }
                if(json.has("comments") && !json.isNull("comments")){
                    JSONArray commentArr = json.optJSONArray("comments");
                    for (int n=0; n<commentArr.length(); n++) {
                        JSONObject jsonObj = commentArr.getJSONObject(n);
                        CommentData cd = new CommentData();
                        cd.toJson(jsonObj);
                        commentDataList.add(cd);
                    }
                }
                    if (json.has("launchDate")) {
                        setLaunchDate(json.getString("launchDate"));
                    }
                    if (json.has("launchStatus")) {
                        setLaunchEventStatus(json.getString("launchStatus"));
                    }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
