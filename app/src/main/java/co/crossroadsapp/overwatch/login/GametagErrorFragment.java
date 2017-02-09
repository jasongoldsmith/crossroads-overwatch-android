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

import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.data.GeneralServerError;

/**
 * Created by karagdi on 1/26/17.
 */
public class GametagErrorFragment extends Fragment {
    public static GametagErrorFragment newInstance(String userTag, Object exception) {
        GametagErrorFragment fragment = new GametagErrorFragment();
        Bundle b = new Bundle();
        if (exception != null && exception instanceof GeneralServerError) {
            GeneralServerError error = (GeneralServerError) exception;
            b.putParcelable("login_error", error);
        }
        if (userTag != null && userTag.length() > 0) {
            b.putString("user_tag", userTag);
        }
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.gametag_error_layout, container, false);
        setUpContactUs(v);
        setUpGameTag(v);
        setUpErrorMessage(v);
        setUpErrorMessageTitle(v);
        setUpBackButton(v);
        return v;
    }

    void setUpErrorMessage(View v) {
        if (v != null) {
            TextView error_message = (TextView) v.findViewById(R.id.error_message);
            if (error_message != null) {
                error_message.setText(getErrorMessage());
            }
        }
    }

    void setUpErrorMessageTitle(View v) {
        if (v != null) {
            TextView already_taken = (TextView) v.findViewById(R.id.already_taken);
            if (already_taken != null) {
                already_taken.setText(getErrorMessageTitle());
            }
        }
    }

    private void setUpGameTag(View v) {
        if (v != null) {
            TextView gametag = (TextView) v.findViewById(R.id.message_title);
            if (gametag != null) {
                gametag.setText(getUserTag());
            }
        }
    }

    private void setUpContactUs(View v) {
        if (v != null) {
            View contact_us = v.findViewById(R.id.contact_us);
            if (contact_us != null) {
                contact_us.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = (Activity) v.getContext();
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ContactUsFragment fragment = ContactUsFragment.newInstance();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                                transaction.replace(R.id.container, fragment, ChooseYourPlatformFragment.class.getSimpleName());
                                transaction.commit();
                            }
                        });
                    }
                });
            }
        }
    }

    public String getErrorMessageTitle() {
        Bundle b = getArguments();
        if (b != null && b.containsKey("login_error")) {
            GeneralServerError error = b.getParcelable("login_error");
            if (error != null && error.getErrorDetails() != null && error.getErrorDetails().getTitle() != null) {
                return error.getErrorDetails().getTitle();
            }
        }
        return getString(R.string.already_taken);
    }

    public String getUserTag() {
        Bundle b = getArguments();
        if (b != null && b.containsKey("user_tag")) {
            return b.getString("user_tag");
        }
        return getString(R.string.gamertag_entered);
    }

    public String getErrorMessage() {
        Bundle b = getArguments();
        if (b != null && b.containsKey("login_error")) {
            GeneralServerError error = b.getParcelable("login_error");
            if (error != null && error.getErrorDetails() != null && error.getErrorDetails().getMessage() != null) {
                return error.getErrorDetails().getMessage();
            }
        }
        return String.format(getString(R.string.account_already_exists_message), getUserTag());
    }

    void setUpBackButton(View v) {
        if (v != null) {
            View back_btn = v.findViewById(R.id.back_btn);
            if (back_btn != null) {
                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Activity activity = (Activity) v.getContext();
                        if (activity == null) {
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
