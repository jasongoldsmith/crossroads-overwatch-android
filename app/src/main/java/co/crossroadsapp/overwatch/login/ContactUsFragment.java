package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.crossroadsapp.overwatch.R;

/**
 * Created by karagdi on 1/26/17.
 */

public class ContactUsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.constact_us_layout, container, false);
        setUpSendUs(v);
        return v;
    }

    private void setUpSendUs(View v) {
        if( v != null )
        {
            View send_us_btn = v.findViewById(R.id.send_us_btn);
            if( send_us_btn != null )
            {
                send_us_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = (Activity) v.getContext();
                        if( activity == null || activity.isFinishing() )
                        {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });
            }
        }
    }

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }
}