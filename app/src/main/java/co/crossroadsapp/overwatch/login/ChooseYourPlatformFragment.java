package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.AddConsoleNetwork;
import co.crossroadsapp.overwatch.utils.TravellerLog;

/**
 * Created by karagdi on 1/24/17.
 */

public class ChooseYourPlatformFragment extends Fragment implements Observer {
    private Spinner _spinner;
    private EditText _battle_tag_input;
    View _sign_with_battlenet;
    private Map<String, String> _platformKeys;
    private Activity _activity;

    public static Fragment newInstance() {
        ChooseYourPlatformFragment fragment = new ChooseYourPlatformFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.choose_your_platform_layout, container, false);
        setUpBattleTagInput(v);
        setUpBattleNetOption(v);
        setUpDropDown(v);
        setUpBackButton(v);
        this._activity = getActivity();
        return v;
    }

    private void setUpDropDown(View v) {
        if (v != null) {
            this._spinner = (Spinner) v.findViewById(R.id.spinner);
            if (this._spinner != null) {
                final String[] data = getResources().getStringArray(R.array.platform_array_keys);
                final String[] values = getResources().getStringArray(R.array.platform_array_values);
                if (data != null && data.length >= values.length) {
                    this._platformKeys = new HashMap<>();
                    for (int i = 0; i < data.length; i++) {
                        this._platformKeys.put(data[i], values[i]);
                    }
                }
                ArrayAdapter<String> adapter = new PlatformAdapter(v.getContext(), android.R.layout.simple_spinner_item, data);
                adapter.setDropDownViewResource(R.layout.empty_layout);
                this._spinner.setAdapter(adapter);
            }
        }
    }

    private void switchLoginOptions(View v, int position) {
        if (position == 0) {
            setUpBattleNetOption(v);
            if (this._battle_tag_input != null) {
                this._battle_tag_input.setVisibility(View.GONE);
                this._battle_tag_input.setOnClickListener(null);
            }
        } else {
            if (this._sign_with_battlenet != null) {
                this._sign_with_battlenet.setVisibility(View.GONE);
                this._sign_with_battlenet.setOnClickListener(null);
            }
            setUpBattleTagInput(v);
        }
    }

    public class PlatformAdapter<String> extends ArrayAdapter<String> {
        public PlatformAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomDropDownView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.choose_platform_spinner_item, parent, false);
            TextView label = (TextView) row.findViewById(R.id.spinner_item_id);
            if (label != null) {
                label.setText("" + getItem(position));
            }
            switchLoginOptions(ChooseYourPlatformFragment.this.getView(), position);
            return row;
        }

        public View getCustomDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.choose_platform_dropdown, parent, false);
            TextView label = (TextView) row.findViewById(R.id.activity_checkpoint_text);
            String text = getItem(position);
            if (text != null) {
                label.setText("" + getItem(position));
            }
            return row;
        }
    }

    @Override
    public void update(Observable o, final Object arg) {
        TravellerLog.w(this, "update: arg: " + arg);
        if (arg instanceof UserData) {
            UserData user = (UserData) arg;
            ControlManager.getmInstance().setUserdata(user);
            Activity act = getActivity();
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

    private void exitLoginPage(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.setResult(Activity.RESULT_OK, new Intent());
        activity.finish();
    }

    private void refreshSignInButton(boolean enabled) {
        TextView join_crossroads = (TextView) getActivity().findViewById(R.id.join_crossroads);
        if (join_crossroads != null) {
            join_crossroads.setEnabled(enabled);
            if (enabled) {
                join_crossroads.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Activity activity = (Activity) v.getContext();
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String selectedItem = (String) _spinner.getSelectedItem();
                                if (_platformKeys != null && _platformKeys.containsKey(selectedItem)) {
                                    addConsole(activity, _platformKeys.get(selectedItem), _battle_tag_input.getText().toString());
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private void setUpBattleNetOption(View v) {
        if (v != null) {
            this._sign_with_battlenet = v.findViewById(R.id.sign_with_battlenet);
            if (this._sign_with_battlenet != null) {
                this._sign_with_battlenet.setVisibility(View.VISIBLE);
                this._sign_with_battlenet.setOnClickListener(new View.OnClickListener() {
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
                                LoginFragment fragment = LoginFragment.newInstance();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                                transaction.replace(R.id.container, fragment, GametagErrorFragment.class.getSimpleName());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    }
                });
            }
        }
    }

    private void setUpBattleTagInput(View v) {
        if (v != null) {
            this._battle_tag_input = (EditText) v.findViewById(R.id.battle_tag_input);
            if (this._battle_tag_input != null) {
                this._battle_tag_input.setVisibility(View.VISIBLE);
                this._battle_tag_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        refreshSignInButton(validateBattleTagInput());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
    }

    private boolean validateBattleTagInput() {
        String pass = this._battle_tag_input.getText().toString();
        if (pass.length() < 4) {
            return false;
        }
        return true;
    }

    private void addConsole(Activity activity, String consoleType, String consoleId) {
        AddConsoleNetwork addConsoleNetwork = new AddConsoleNetwork(activity);
        addConsoleNetwork.addObserver(this);
        try {
            addConsoleNetwork.addConsole(consoleType, consoleId);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            update(addConsoleNetwork, null);
        }
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
