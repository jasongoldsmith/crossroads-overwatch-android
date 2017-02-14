package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.data.GeneralServerError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.ReportCrashNetwork;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 1/26/17.
 */

public class ContactUsFragment extends Fragment implements Observer {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.constact_us_layout, container, false);
        setUpBackButton(v);
        setUpSendUs(v);
        return v;
    }

    private void setUpSendUs(View v) {
        if( v != null ) {
            View send_us_btn = v.findViewById(R.id.send_us_btn);
            final EditText email_input = (EditText) v.findViewById(R.id.email_input);
            final EditText comment_input = (EditText) v.findViewById(R.id.user_message);

            if( send_us_btn != null )
            {
                send_us_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Activity activity = (Activity) v.getContext();
                        if( activity == null || activity.isFinishing() )
                        {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postComment((Activity)v.getContext(), email_input.getText().toString(), comment_input.getText().toString());
                            }
                        });
                    }
                });
            }
        }
    }

    private boolean validateMessage(String msg) {
        if (msg.length() < 1) {
            return false;
        }
        return true;
    }

    private boolean validateUsername(String email) {
        if (email.contains("@") || email.contains(".")) {
            return true;
        }
        return false;
    }

    private void postComment(final Activity activity, String email, String userMsg) {
        if(validateUsername(email) && validateMessage(userMsg)) {
            ReportCrashNetwork crashReportNetwork = new ReportCrashNetwork(activity);
            crashReportNetwork.addObserver(this);
            try {
                crashReportNetwork.doCrashReport(email, userMsg);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

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
                            exitLoginPage();
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
                    if (exception != null && exception instanceof GeneralServerError) {
                        GeneralServerError error = (GeneralServerError) exception;

                    }
                    if (userTag != null && userTag.length() > 0) {

                    }
                }
            });
        }
    }

    protected void exitLoginPage() {
        Activity activity = getActivity();
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