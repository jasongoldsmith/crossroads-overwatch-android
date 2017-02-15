package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.utils.Util;

/**
 * Created by karagdi on 1/24/17.
 */

public abstract class AbstractTravellerLoginFragment extends PreChooseGameTagFragment {
    private EditText _password_input;
    private EditText _email_input;

    public View buildView(LayoutInflater inflater, @Nullable ViewGroup container, int resId) {
        return inflater.inflate(resId, container, false);
    }

    protected void setUpEmailInput(View v) {
        if (v != null) {
            this._email_input = (EditText) v.findViewById(R.id.email_input);
            if (this._email_input != null) {
                this._email_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        refreshSignInButton(true);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
    }

    String getUsername() {
        if (this._email_input != null) {
            return _email_input.getText().toString();
        }
        return null;
    }

    protected void setUpPasswordInput(View v) {
        if (v != null) {
            this._password_input = (EditText) v.findViewById(R.id.password_input);
            if (this._password_input != null) {
                this._password_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //refreshSignInButton(validateCredentials());
                        refreshSignInButton(true);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                this._password_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (validateUsername()) {
                                executeLogin((Activity) v.getContext(), _email_input.getText().toString(), _password_input.getText().toString());
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    protected void setUpChangePasswordToLetters(final View v) {
        if (v != null) {
            View change_password_to_letters = v.findViewById(R.id.change_password_to_letters);
            if (change_password_to_letters != null) {
                change_password_to_letters.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText password_input = (EditText) v.findViewById(R.id.password_input);
                        if (password_input != null) {
                            if (isPasswordInputType(password_input.getInputType())) {
                                password_input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                            } else {
                                password_input.setInputType((EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD));
                            }
                        }
                    }
                });
            }
        }
    }

    public boolean validateCredentials() {
        if (this._email_input == null || this._password_input == null) {
            return false;
        } else {
            return validateUsername() && validatePassword();
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

    private boolean validatePassword() {
        String pass = this._password_input.getText().toString();
//        if (pass.length() < 4) {
//            return false;
//        }
        return true;
    }

    private boolean validateUsername() {
        String user = this._email_input.getText().toString();
        if (user.contains("@") || user.contains(".")) {
            return true;
        }
        showError("ERROR", "Please enter a valid email address");
        return false;
    }

    static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void executeLogin(final Activity activity, final String email, final String password) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postLogin(activity, email, password);
            }
        });
    }

    private void refreshSignInButton(boolean enabled) {
        TextView sign_in_btn = (TextView) getActivity().findViewById(R.id.sign_in_btn);
        if (sign_in_btn != null) {
            sign_in_btn.setEnabled(enabled);
            if (enabled) {
                sign_in_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validateUsername()) {
                            executeLogin((Activity) v.getContext(), _email_input.getText().toString(), _password_input.getText().toString());
                        }
                    }
                });
            }
        }
    }

    abstract void postLogin(Activity activity, String email, String password);
}
