package co.crossroadsapp.overwatch;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import co.crossroadsapp.overwatch.data.EventData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sharmha on 3/25/16.
 */
public class NotificationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        String payload = null;
        String alert = null;
        //get the data using the keys you entered at the service
        Bundle extra = i.getExtras();
        if (extra != null) {
            alert = extra.getString("message");
            payload = extra.getString("payload");
        }

        JSONObject jsonObj = null;
        try {
            jsonObj = (payload != null && !payload.equals(""))?new JSONObject(payload): new JSONObject();
            EventData ed = new EventData();
            Intent in = new Intent("subtype_flag");

            if(jsonObj.has("notificationName") && !jsonObj.isNull("notificationName")) {
                String notiName = (String) jsonObj.get("notificationName");
                if (notiName.equalsIgnoreCase("messageFromPlayer")) {
                    in.putExtra("playerMessage", true);
                }
            }

            if(jsonObj.has("eventName") && !jsonObj.isNull("eventName")) {
                String eName = (String) jsonObj.get("eventName");
                if (!eName.equalsIgnoreCase(null)) {
                        in.putExtra("subtype", eName);
                    }
            }

            if(jsonObj.has("eventId") && !jsonObj.isNull("eventId")) {
                String eId = (String) jsonObj.get("eventId");
                if (!eId.equalsIgnoreCase(null)) {
                    in.putExtra("eventId", eId);
                }
            }
            if(jsonObj.has("eventUpdated") && !jsonObj.isNull("eventUpdated")) {
                String eUpdated = (String) jsonObj.get("eventUpdated");
                if (!eUpdated.equalsIgnoreCase(null)) {
                    in.putExtra("eventUpdated", eUpdated);
                }
            }
            if(jsonObj.has("eventClanName") && !jsonObj.isNull("eventClanName")) {
                String eClanName = (String) jsonObj.get("eventClanName");
                if (!eClanName.equalsIgnoreCase(null)) {
                    in.putExtra("eventClanName", eClanName);
                }
            }
            if(jsonObj.has("eventClanImageUrl") && !jsonObj.isNull("eventClanImageUrl")) {
                String eClanImage = (String) jsonObj.get("eventClanImageUrl");
                if (!eClanImage.equalsIgnoreCase(null)) {
                    in.putExtra("eventClanImageUrl", eClanImage);
                }
            }
            if(jsonObj.has("eventConsole") && !jsonObj.isNull("eventConsole")) {
                String eConsole = (String) jsonObj.get("eventConsole");
                if (!eConsole.equalsIgnoreCase(null)) {
                    in.putExtra("eventConsole", eConsole);
                }
            }
            if(jsonObj.has("eventClanId") && !jsonObj.isNull("eventClanId")) {
                String eClanId = (String) jsonObj.get("eventClanId");
                if (!eClanId.equalsIgnoreCase(null)) {
                    in.putExtra("eventClanId", eClanId);
                }
            }
            if(jsonObj.has("messengerConsoleId") && !jsonObj.isNull("messengerConsoleId")) {
                    String mClanId = (String) (jsonObj.get("messengerConsoleId"));
                    if (!mClanId.equalsIgnoreCase(null)) {
                        in.putExtra("messengerConsoleId", mClanId);
                    }
            }
            if(jsonObj.has("messengerImageUrl") && !jsonObj.isNull("messengerImageUrl")) {
                    String mImageUrl = (String) (jsonObj.get("messengerImageUrl"));
                    if (!mImageUrl.equalsIgnoreCase(null)) {
                        in.putExtra("messengerImageUrl", mImageUrl);
                    }
            }
            if(alert!=null && alert.length()>0){
                in.putExtra("message", alert);
            }
            sendBroadcast(in);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Service.START_NOT_STICKY;
    }
}
