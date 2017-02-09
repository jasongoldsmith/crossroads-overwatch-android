package co.crossroadsapp.overwatch.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sharmha on 3/31/16.
 */
public class SendBackpressBroadcast extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        Intent in = new Intent("backpress_flag");
        sendBroadcast(in);
        return Service.START_NOT_STICKY;
    }
}
