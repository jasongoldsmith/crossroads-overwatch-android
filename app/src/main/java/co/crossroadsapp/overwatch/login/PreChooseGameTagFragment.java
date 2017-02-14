package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.BattletagAlreadyTakenException;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.core.TrimbleException;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 1/27/17.
 */

public class PreChooseGameTagFragment extends Fragment implements Observer {
    Activity _activity;

    @Override
    public void update(Observable o, final Object arg) {
        TravellerLog.w(this, "update: arg: " + arg);
        if (arg instanceof UserData) {
            UserData user = (UserData) arg;
            if (user != null) {
                if (Util.ensureGameTag(user)) {
                    ControlManager.getmInstance().setUserdata(user);
                    Activity act = getActivity();
                    if (act == null || act.isFinishing()) {
                        return;
                    }
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exitLoginPage(_activity);
                        }
                    });
                } else {
                    Activity act = getActivity();
                    if (act == null || act.isFinishing()) {
                        return;
                    }
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ChooseYourPlatformFragment fragment = new ChooseYourPlatformFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                            transaction.replace(R.id.container, fragment);
                            transaction.commit();
                        }
                    });
                }
            }
        } else {
//            if(arg instanceof OverwatchLoginException) {
//                ((BattletagAlreadyTakenException) arg).getMessage()
//            }
            Activity act = getActivity();
            if (act == null || act.isFinishing()) {
                return;
            }
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Object exception = null;
                    String userTag = null;
                    if( arg != null && arg instanceof OverwatchLoginException)
                    {
                        exception = ((OverwatchLoginException) arg).getCustomData();
                        userTag = ((OverwatchLoginException) arg).getUserTag();
                    }
                    GametagErrorFragment fragment = GametagErrorFragment.newInstance(userTag, exception);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    transaction.replace(R.id.container, fragment, GametagErrorFragment.class.getSimpleName());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }

    protected void exitLoginPage(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.setResult(Activity.RESULT_OK, new Intent());
        activity.finish();
    }

    void setUpBackButton(View v) {
        if( v != null )
        {
            View back_btn = v.findViewById(R.id.back_btn);
            if( back_btn != null )
            {
                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Activity activity = (Activity) v.getContext();
                        if( activity == null )
                        {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.onBackPressed();
                            }
                        });
                    }
                });
            }
        }
    }
}
