package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.data.GeneralServerError;
import co.crossroadsapp.overwatch.data.LoginError;
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
            ControlManager cManager = ControlManager.getmInstance();
            View send_us_btn = v.findViewById(R.id.send_us_btn);
            final EditText email_input = (EditText) v.findViewById(R.id.email_input);
            final EditText comment_input = (EditText) v.findViewById(R.id.user_message);

            if(cManager!=null && cManager.getUserData()!=null) {
                UserData user = cManager.getUserData();
                if(user.getEmail()!=null && !user.getEmail().isEmpty()) {
                    email_input.setText(user.getEmail());
                    email_input.setKeyListener(null);
                }
            }
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
        if (arg != null) {
            if(arg instanceof LoginError) {
                String msg = "Server error.";
                String title = "ERROR";
                if(((LoginError)arg).getGeneralServerError()!=null && ((LoginError)arg).getGeneralServerError().getErrorDetails()!=null) {
                    if(((LoginError)arg).getGeneralServerError().getErrorDetails().getMessage()!=null) {
                        msg = ((LoginError)arg).getGeneralServerError().getErrorDetails().getMessage();
                    }
                    if(((LoginError)arg).getGeneralServerError().getErrorDetails().getTitle()!=null) {
                        title = ((LoginError)arg).getGeneralServerError().getErrorDetails().getTitle();
                    }
                    showError(title, msg);
                }
            }
        } else {
            exitLoginPage();
        }
    }

    private void showError(final String titleText, final String msgText) {
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
                Util.showErrorMsg(error, msg, title, titleText, msgText);
            }
        };

        handler.post(r);
    }

    protected void exitLoginPage() {
        final Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
//        activity.setResult(Activity.RESULT_OK, new Intent());
//        activity.finish();
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
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                });
            }
        }
    }
}