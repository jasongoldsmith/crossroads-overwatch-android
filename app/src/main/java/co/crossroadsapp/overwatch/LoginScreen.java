package co.crossroadsapp.overwatch;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import co.crossroadsapp.overwatch.login.AddLinkedAccount;
import co.crossroadsapp.overwatch.login.ChooseYourPlatformFragment;
import co.crossroadsapp.overwatch.login.TravellerLoginFragment;
import co.crossroadsapp.overwatch.login.TravellerSignupFragment;
import co.crossroadsapp.overwatch.utils.Constants;

/**
 * Created by karagdi on 1/18/17.
 */
public class LoginScreen extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTRansparentStatusBar();
        Intent intent = getIntent();
        int screen = Constants.LOGIN_SCREEN_VALUE;
        if (intent != null) {
            screen = intent.getIntExtra(Constants.LOGIN_SIGNUP_SCREEN_KEY, Constants.LOGIN_SCREEN_VALUE);
        }
        //setContentView((screen == Constants.LOGIN_SCREEN_VALUE ? R.layout.login_layout : R.layout.login_signup_layout));
        setContentView(R.layout.login_layout);
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = fm.beginTransaction();
            if (screen == Constants.ENTER_GAMETAG_NAME_VALUE) {
                transaction.replace(R.id.container, getCurrentFragment(screen), ChooseYourPlatformFragment.class.getSimpleName());
                transaction.addToBackStack(ChooseYourPlatformFragment.class.getSimpleName());
            } else {
                transaction.replace(R.id.container, getCurrentFragment(screen));
            }
            //*.replace(R.id.container, getCurrentFragment(3))*/
            transaction.commit();
        }
    }

    private Fragment getCurrentFragment(int screen) {
        if (screen == Constants.LOGIN_SCREEN_VALUE) {
            return TravellerLoginFragment.newInstance();
        } else if (screen == Constants.SIGNUP_SCREEN_VALUE) {
            return TravellerSignupFragment.newInstance();
        } else if (screen == Constants.ENTER_GAMETAG_NAME_VALUE) {
            return ChooseYourPlatformFragment.newInstance();
        } else if (screen == Constants.ADD_LINKED_ACCOUNT_VALUE) {
            return AddLinkedAccount.newInstance();
        } else {
            return TravellerSignupFragment.newInstance();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null && fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry tmp = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            Fragment f = fragmentManager.findFragmentByTag(tmp.getName());
            if (f == null) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                setResult(Constants.LOGIN_SCREEN_BACK_BUTTON);
                finish();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTRansparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
