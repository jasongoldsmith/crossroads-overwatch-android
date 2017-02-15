package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.crossroadsapp.overwatch.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PasswordSent OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PasswordSent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordSent extends Fragment {
    //private ImageView back;
//
//    public PasswordSent() {
//        // Required empty public constructor
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PasswordSent.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordSent newInstance() {
        PasswordSent fragment = new PasswordSent();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_password_sent, container, false);
        setupBackButton(v);
        return v;
    }

    private void setupBackButton(View v) {
        if( v != null )
        {
            View back_btn = v.findViewById(R.id.back);
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
