package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.core.OverwatchLoginException;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.AddConsoleNetwork;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 2/2/17.
 */
public class AddLinkedAccount extends AbstractTravellerLoginFragment {
    private Spinner _spinner;
    private EditText _battle_tag_input;
    View _sign_with_battlenet;
    private Map<String, String> _platformKeys;
    private String _pcName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = buildView(inflater, container, R.layout.add_linked_account_layout);
        setUpBattleTagInput(v);
        setUpBattleNetOption(v);
        setUpDropDown(v);
        setUpBackButton(v);
        this._activity = getActivity();
        return v;
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
                        if (activity == null || activity.isFinishing()) {
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

    private void setUpDropDown(View v) {
        if (v != null) {
            this._spinner = (Spinner) v.findViewById(R.id.add_linked_spinner);
            if (this._spinner != null) {
                String[] tmpKeys = getResources().getStringArray(R.array.platform_array_keys);
                if (tmpKeys != null && tmpKeys.length > 0) {
                    this._pcName = tmpKeys[0];
                }
                final String[] data = Util.buildConsoleList(getResources().getStringArray(R.array.platform_array_values), tmpKeys);
                final String[] values = Util.buildConsoleList(getResources().getStringArray(R.array.platform_array_values), getResources().getStringArray(R.array.platform_array_values));
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

    private void refreshSignInButton(boolean enabled) {
        TextView link_to_crossroads = (TextView) getActivity().findViewById(R.id.link_to_crossroads);
        if (link_to_crossroads != null) {
            link_to_crossroads.setEnabled(enabled);
            if (enabled) {
                link_to_crossroads.setOnClickListener(new View.OnClickListener() {
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
                                    if(checkGamertagValidity(_battle_tag_input.getText().toString(), _platformKeys.get(selectedItem))) {
                                        addLinkedConsole(activity, _platformKeys.get(selectedItem), _battle_tag_input.getText().toString());
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private boolean checkGamertagValidity(String tag, String console) {
        if(console.equalsIgnoreCase("ps4")) {
            if(tag.matches(".*[^a-z^0-9^A-Z\\_\\-].*") || tag.length()>16) {
                showError();
                return false;
            }
        } else if(console.equalsIgnoreCase("xboxone")) {
            if(tag.matches(".*[^a-z^0-9^A-Z^ ].*") || tag.length()>16 || tag.length()<1 || tag.startsWith(" ") || tag.contains("  ") || Character.isDigit(tag.charAt(0))){
                showError();
                return false;
            }
        } else {
            return true;
        }
        return false;
    }

    private void showError() {
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
                Util.showErrorMsg(error, msg, title, "Please enter a valid gamertag.", "INVALID GAMERTAG");
            }
        };

        handler.post(r);
    }

    private void addLinkedConsole(Activity activity, String consoleType, String consoleId) {
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

    @Override
    void postLogin(Activity activity, String email, String password) {
        TravellerLog.w(this, "postLogin");
    }

    @Override
    public void update(Observable o, final Object arg) {
        TravellerLog.w(this, "update: arg: " + arg);
        if (arg instanceof UserData) {
            ControlManager.getmInstance().setUserdata((UserData) arg);
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
                    if (arg != null && arg instanceof OverwatchLoginException) {
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
        AddLinkedAccount fragment = new AddLinkedAccount();
        return fragment;
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
            String lableName = getItem(position);
            if (label != null) {
                label.setText("" + lableName);
            }
            switchLoginOptions(AddLinkedAccount.this.getView(), position, (java.lang.String) lableName);
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

    private void switchLoginOptions(View v, int position, String item) {
        if (position == 0 && this._pcName != null && this._pcName.equalsIgnoreCase(item)) {
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
}
