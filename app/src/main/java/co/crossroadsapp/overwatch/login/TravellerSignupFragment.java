package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.TravellerSignUpNetwork;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 1/24/17.
 */

public class TravellerSignupFragment extends AbstractTravellerLoginFragment implements Observer {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = buildView(inflater, container, R.layout.traveller_signup_layout);
        setUpEmailInput(v);
        setUpPasswordInput(v);
        setUpChangePasswordToLetters(v);
        setUpPrivacyText(v);
        setUpBackButton(v);
        return v;
    }

    private void setUpPrivacyText(View v) {
        if( v != null ) {
            TextView privacyTerms = (TextView) v.findViewById(R.id.sign_up_terms_privacy);
            Util.setTextViewHTML(privacyTerms, getString(R.string.sign_up_terms_privacy));
        }
    }

    @Override
    void postLogin(Activity activity, String email, String password) {
        TravellerSignUpNetwork signupNetwork = new TravellerSignUpNetwork(activity);
        signupNetwork.addObserver(this);
        try {
            signupNetwork.doSignup(email, password);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            update(signupNetwork, null);
        }
    }

    @Override
    public void update(Observable o, final Object arg) {
        TravellerLog.w(this, "update: arg: " + arg);
        if( arg instanceof UserData )
        {
            Activity act = getActivity();
            if( act == null || act.isFinishing() )
            {
                return;
            }
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ChooseYourPlatformFragment fragment = new ChooseYourPlatformFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    transaction.replace(R.id.container, fragment, ChooseYourPlatformFragment.class.getSimpleName());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        else {
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

    public static Fragment newInstance() {
        TravellerSignupFragment fragment = new TravellerSignupFragment();
        return fragment;
    }
}