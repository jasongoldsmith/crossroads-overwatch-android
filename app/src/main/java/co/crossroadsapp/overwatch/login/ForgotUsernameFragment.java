package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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

import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.network.ForgotPasswordNetwork;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 2/3/17.
 */

public class ForgotUsernameFragment extends Fragment implements Observer {
    private EditText _email_input;

    public static ForgotUsernameFragment newInstance(String username) {
        ForgotUsernameFragment fragment = new ForgotUsernameFragment();
        if( username != null && username.length() > 0 )
        {
            Bundle b = new Bundle();
            b.putString("username", username);
            fragment.setArguments(b);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.forgot_username_layout, container, false);
        setUpEmailInput(v);
        setUpBackButton(v);
        /*setUpBattleNetOption(v);
        setUpDropDown(v);
        this._activity = getActivity();*/
        return v;
    }

    String getUsername()
    {
        Bundle b = getArguments();
        if( b != null && b.containsKey("username"))
        {
            return b.getString("username");
        }
        return null;
    }

    private void setUpBackButton(View v) {
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

    protected void setUpEmailInput(View v) {
        if (v != null) {
            this._email_input = (EditText) v.findViewById(R.id.email_input);
            if (this._email_input != null) {
                String username = getUsername();
                if( username != null && username.length() > 0 )
                {
                    this._email_input.setText(username);
                }
                this._email_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        refreshSignInButton(validateCredentials());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
    }

    public boolean validateCredentials() {
        if (this._email_input == null) {
            return false;
        } else {
            return validateUsername();
        }
    }

    private boolean validateUsername() {
        String user = this._email_input.getText().toString();
        if (user.contains("@") || user.contains(".")) {
            return true;
        }
        return false;
    }

    private void refreshSignInButton(boolean enabled) {
        TextView sign_in_btn = (TextView) getActivity().findViewById(R.id.sign_in_btn);
        if (sign_in_btn != null) {
            sign_in_btn.setEnabled(enabled);
            if (enabled) {
                sign_in_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        executeForgotUsername((Activity) v.getContext(), _email_input.getText().toString());
                    }
                });
            }
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

    protected void executeForgotUsername(Activity activity, String email) {
        ForgotPasswordNetwork resetNetwork = new ForgotPasswordNetwork(activity);
        resetNetwork.addObserver(this);
        try {
            try {
                resetNetwork.doResetPassword(email);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            update(resetNetwork, null);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
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
            final Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PasswordSent fragment = PasswordSent.newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    transaction.replace(R.id.container, fragment, PasswordSent.class.getSimpleName());
                    transaction.commit();
                }
            });
        }
    }
}
