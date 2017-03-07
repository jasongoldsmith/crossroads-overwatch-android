package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.data.ConsoleData;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.TravellerLoginNetwork;
import co.crossroadsapp.overwatch.network.TravellerSignUpNetwork;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 1/20/17.
 */
public class TravellerLoginFragment extends AbstractTravellerLoginFragment {
    public static Fragment newInstance() {
        TravellerLoginFragment fragment = new TravellerLoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = buildView(inflater, container, R.layout.traveller_login_layout);
        setUpEmailInput(v);
        setUpPasswordInput(v);
        setUpChangePasswordToLetters(v);
        setTextViewHTML((TextView) v.findViewById(R.id.sign_up_terms_privacy), getString(R.string.sign_up_terms_privacy));
        setUpForgotUsername(v);
        setUpBackButton(v);
        this._activity = getActivity();
        return v;
    }

    private void setUpForgotUsername(View v) {
        if (v != null) {
            View forgot_layout = v.findViewById(R.id.forgot_layout);
            if (forgot_layout != null) {
                forgot_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Activity activity = (Activity) v.getContext();
                        if (activity == null) {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeKeyboard(activity);
                                ForgotUsernameFragment fragment = ForgotUsernameFragment.newInstance(getUsername());
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                                transaction.replace(R.id.container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    }
                });
            }
        }
    }

    private void closeKeyboard(Activity activity) {
        View v = getView();
        if (v != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    protected void setTextViewHTML(TextView title, String html) {
        if (title != null) {
            title.setMovementMethod(LinkMovementMethod.getInstance());
            if (Util.hasNougat()) {
                title.setText(Html.fromHtml(getString(R.string.terms_conditions), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));// for 24 api and more
            } else {
                title.setText(Html.fromHtml(getString(R.string.terms_conditions))); // or for older api
            }
        }
    }

    @Override
    protected void postLogin(Activity activity, String email, String password) {
        TravellerLoginNetwork loginNetwork = new TravellerLoginNetwork(activity);
        loginNetwork.addObserver(this);
        try {
            loginNetwork.doLogin(email, password);
        } catch (JSONException e) {
            update(loginNetwork, null);
        } catch (UnsupportedEncodingException e) {
            update(loginNetwork, null);
        }
    }
}
