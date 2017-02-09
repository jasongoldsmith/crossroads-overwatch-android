package co.crossroadsapp.overwatch;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by sharmha on 3/14/16.
 */
public class MyGcmBroadcastReceiver extends BroadcastReceiver {

    private static final String GCM_RECEIVE_INTENT = "com.google.android.c2dm.intent.RECEIVE";
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        TravellerLog.w(TAG, "received from gcm");
        if (intent.getAction().equals(GCM_RECEIVE_INTENT)) {
            String pushDataString = intent.getStringExtra("data");
            Bundle i = intent.getExtras();
            JSONObject pushData = null;
            String alert = null;
            String payload = null;
            if (i != null) {
                    //pushData = new JSONObject(pushDataString);
                    alert = i.getString("message");
                    payload = i.getString("payload");
            }
            if (isAppRunning(context)) {
                final Intent in = new Intent(context, NotificationService.class);
                in.putExtra("payload", payload);
                in.putExtra("message", alert);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(in);
//                Thread t = new Thread(){
//                    public void run(){
//                        context.startService(in);
//                    }
//                };
//                t.start();
            } else {
//                Intent i = new Intent(context, UpdateCacheService.class);
//                i.putExtra("message", payload);
//                i.putExtra("alert", alert);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startService(i);
                //GCMResponse gcmMessage = Utils.readGCMResponse(payload);
//                if( gcmMessage == null )
//                {
//                    return;
//                }
                //int notificationCounter = Utils.readNewNotificationCount();
                Intent notificationIntent = createNotificationIntent(context, alert, payload);
                PendingIntent resultIntent = PendingIntent.getActivity(context, Constants.INTENT_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                if(alert!=null) {
                    mBuilder.setAutoCancel(true).setSmallIcon(R.drawable.img_traveler_badge_icon).setContentIntent(resultIntent).setContentTitle(context.getResources().getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText(alert)).setContentText(alert);
                }else{
                    mBuilder.setSmallIcon(R.drawable.img_traveler_badge_icon).setContentIntent(resultIntent).setContentText("New Message Received").setContentTitle(context.getResources().getString(R.string.app_name));
                }
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(UUID.randomUUID().hashCode(), mBuilder.build());
                //Utils.storeNewNotificationCount(notificationCounter);
            }
        }
    }

    private Intent createNotificationIntent(Context ctxt, String alert, String payload) {
        // notificationIntent
        Intent notificationIntent = new Intent(ctxt, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mark intent as notification
        notificationIntent.putExtra(Constants.TRAVELER_NOTIFICATION_INTENT, Constants.TRAVELER_NOTIFICATION_INTENT);
        // SplashScreen Intent
        Intent launchIntent = new Intent(ctxt, MainActivity.class);
        if (alert != null && alert.length() > 0 && payload != null && payload.length() > 0) {
            Intent contentIntent = new Intent();
            contentIntent.putExtra("message", payload);
            contentIntent.putExtra("alert", alert);
            contentIntent.putExtra("id", Constants.INTENT_ID);
            launchIntent.putExtra(Constants.NOTIFICATION_INTENT_CHANNEL, contentIntent);
        }
        notificationIntent.putExtra(Constants.TRAVELER_NOTIFICATION_INTENT, launchIntent);
        return notificationIntent;
    }

    public boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        if (services != null && services.size() > 0 && services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            return true;
        }
        return false;
    }
}
