package co.crossroadsapp.overwatch.data;

/**
 * Created by sharmha on 7/26/16.
 */
public class PushNotification {
    private String eId=null;
    private String eUpdated=null;
    private boolean typeMessage=false;
    private String eName =null;
    private String message=null;
    private String eventClanId=null;
    private String eventClanName=null;
    private String eventClanImageUrl=null;
    private String eventConsole=null;
    private String messengerConsoleId=null;
    private String messengerImageUrl=null;

    public void seteId(String id) {
        eId = id;
    }

    public String geteId() {
        return this.eId;
    }

    public void seteUpdated(String update) {
        eUpdated = update;
    }

    public String geteUpdated() {
        return this.eUpdated;
    }

    public void seteName(String name) {
        eName = name;
    }

    public String geteName() {
        return this.eName;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public void setTypeMessage(boolean tMsg) {
        typeMessage = tMsg;
    }

    public boolean getTypeMessage() {
        return this.typeMessage;
    }

    public void setEventClanId(String clanId) {
        eventClanId = clanId;
    }

    public String getEventClanId() {
        return this.eventClanId;
    }

    public void setEventClanName(String clanName) {
        eventClanName = clanName;
    }

    public String getEventClanName() {
        return this.eventClanName;
    }

    public void setEventClanImageUrl(String clanUrl) {
        eventClanImageUrl = clanUrl;
    }

    public String getEventClanImageUrl() {
        return this.eventClanImageUrl;
    }

    public void setMessengerConsoleId(String consoleMsgr) {
        messengerConsoleId = consoleMsgr;
    }

    public String getMessengerConsoleId() {
        return this.messengerConsoleId;
    }

    public void setMessengerImageUrl(String msngrUrl) {
        messengerImageUrl = msngrUrl;
    }

    public String getMessengerImageUrl() {
        return this.messengerImageUrl;
    }

    public void setEventConsole(String console) {
        eventConsole = console;
    }

    public String getEventConsole() {
        return this.eventConsole;
    }
}
