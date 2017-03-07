package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.BattletagAlreadyTakenException;
import co.crossroadsapp.overwatch.core.GeneralErrorException;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.core.TrimbleException;
import co.crossroadsapp.overwatch.data.GeneralServerError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
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
                    //user logged in
                    Util.setDefaults("loggedin", "true", act);
                    Util.setDefaults("opendrawer", "true", act);
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
            final Activity act = getActivity();
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
                    if(arg instanceof GeneralErrorException) {
                        if (exception != null && exception instanceof GeneralServerError) {
                            GeneralServerError error = (GeneralServerError) exception;
                            showError(error.getErrorDetails().getTitle(), error.getErrorDetails().getMessage());
                        }
                    } else {

                        GametagErrorFragment fragment = GametagErrorFragment.newInstance(userTag, exception, Constants.LOGIN_ERROR);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        transaction.replace(R.id.container, fragment, GametagErrorFragment.class.getSimpleName());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
    }

    public void showError(final String titleText, final String msgText) {
        final RelativeLayout error = (RelativeLayout) getActivity().findViewById(R.id.error_layout);
        final TextView msg = (TextView) getActivity().findViewById(R.id.error_sub);
        final TextView title = (TextView) getActivity().findViewById(R.id.error_text);
        final ImageView close = (ImageView) getActivity().findViewById(R.id.err_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.GONE);
            }
        });

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                Util.showErrorMsg(error, msg, title, msgText, titleText);
            }
        };

        handler.post(r);
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
