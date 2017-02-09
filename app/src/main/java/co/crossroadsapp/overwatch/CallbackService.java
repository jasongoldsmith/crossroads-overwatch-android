package co.crossroadsapp.overwatch;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.facebook.applinks.AppLinkData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;

import java.util.Map;

public class CallbackService extends Service {

    public CallbackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        final ControlManager cManager = ControlManager.getmInstance();
        final Handler mHandler = new Handler();

        if(cManager.getCurrentActivity()!=null) {
            AppLinkData.fetchDeferredAppLinkData(cManager.getCurrentActivity(),
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(final AppLinkData appLinkData) {
                            if (appLinkData != null) {
                                // Process app link data
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Map<String, String> jsonFacebook = Util.parseDeferredLink(appLinkData.getTargetUri().toString());
                                        Util.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               postTracking(jsonFacebook, cManager.getCurrentActivity(), cManager, Constants.APP_INSTALL);
                                        Util.setDefaults(Constants.APP_INSTALL, Constants.FACEBOOK_SOURCE, cManager.getCurrentActivity());
                                    }

                                }, 500);
                            }
                        }
                    }
            );

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Util.setOrganicAppInstall(cManager);
                    stopSelf();
                }

            }, 60000);
        }

        return Service.START_NOT_STICKY;
    }
}

