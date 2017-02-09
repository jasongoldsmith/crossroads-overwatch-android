package co.crossroadsapp.overwatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.NetworkEngine;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sharmha on 2/22/16.
 */
public class RegisterActivity extends BaseActivity implements Observer {

    private EditText name_signup;
    private EditText pswd_signup;
    private EditText psnid_signup;
    private ImageView signup_btn;
    private NetworkEngine ntwrk;
    private Util util;
    private UserData user;
    SharedPreferences mPrefs;
    private TextView consoleIdText;

    private ControlManager mManager;

    private String username;
    private String password;
    private String psnid;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        //user = b.getParcelable("userdata");

        final String consoleType = Util.getDefaults("consoleType", getApplicationContext());
        final String memId = Util.getDefaults("membershipId", getApplicationContext());
        final String consoleId = Util.getDefaults("consoleId", getApplicationContext());

//        mPrefs = getPreferences(MODE_PRIVATE);

        setContentView(R.layout.register);

        dialog = new ProgressDialog(this);

        consoleIdText = (TextView) findViewById(R.id.psn);

        setConsoleIdText(consoleType, consoleId);

        name_signup = (EditText) findViewById(R.id.signup_name);
        pswd_signup = (EditText) findViewById(R.id.signup_pswrd);
        pswd_signup.setTypeface(Typeface.DEFAULT);
        pswd_signup.setTransformationMethod(new PasswordTransformationMethod());

        psnid_signup = (EditText) findViewById(R.id.signup_psn);
        signup_btn = (ImageView) findViewById(R.id.signup_btn);

        if((consoleId!=null) && (!consoleId.isEmpty())) {
            psnid_signup.setText(consoleId);
            psnid_signup.setEnabled(false);
        }
//        privacyTerms = (TextView) findViewById(R.id.privacy_terms);
//
//        webView = (WebView) findViewById(R.id.web);

        //Spanned s = Html.fromHtml(getString(R.string.terms_conditions));

//        setTextViewHTML(privacyTerms, getString(R.string.terms_conditions));
//        privacyTerms.setText(Html.fromHtml(getString(R.string.terms_conditions)));
//        privacyTerms.setMovementMethod(LinkMovementMethod.getInstance());
//
        //webView.setWebViewClient(new MyWebViewClient());

//        errLayout = (RelativeLayout) findViewById(R.id.error_layout);
//        errText = (TextView) findViewById(R.id.error_sub);
//        close_err = (ImageView) findViewById(R.id.err_close);
//
//        close_err.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                errLayout.setVisibility(View.GONE);
//            }
//        });

        //ntwrk = new NetworkEngine(getApplicationContext());

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        name_signup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                //name_signup.setHint("");
                v.performClick();
            }
        });

        name_signup.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        pswd_signup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                //pswd_signup.setHint("");
                v.performClick();
            }
        });

        pswd_signup.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        psnid_signup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                //pswd_signup.setHint("");
                v.performClick();
            }
        });

        pswd_signup.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        psnid_signup.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    // the user is done typing.
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    //to hide it, call the method again
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    //signup_btn.performClick();
                }
                return false;
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                username = name_signup.getText().toString();
                password = pswd_signup.getText().toString();
                psnid = psnid_signup.getText().toString();

                if (!username.isEmpty() && !password.isEmpty() && !psnid.isEmpty()) {
                    signup_btn.setImageDrawable(getResources().getDrawable(R.drawable.img_login_btn_tapped));
                    RequestParams params = new RequestParams();
                    params.put("userName", username);
                    params.put("passWord", password);
                    //params.put("psnId", psnid);

                    //testing params for array list
                    List<Map<String, String>> consoles = new ArrayList<Map<String,
                                                String>>();
                    Map<String, String> user1 = new HashMap<String, String>();
                    user1.put("consoleType", consoleType);
                    user1.put("consoleId", consoleId);
                    consoles.add(user1);
                    params.put("consoles", consoles);
                    params.put("bungieMemberShipId", memId);
                    dialog.show();
                    dialog.setCancelable(false);
                    mManager.postLogin(RegisterActivity.this, params, Constants.REGISTER);
                } else {
                    if(username.length()==0){
                        showError(getResources().getString(R.string.username_missing));
                    } else if(username.length() < 3) {
                        showError(getResources().getString(R.string.username_short));
                    } else if(password.length()==0) {
                        showError(getResources().getString(R.string.password_missing));
                    } else if(password.length()<5){
                        showError(getResources().getString(R.string.password_short));
                    } else {
                        showError("Please enter correct PsnId.");
                    }
                }
            }
        });
    }

    private void setConsoleIdText(String consoleType, String consoleId) {
        if ((consoleType!=null) && (consoleId!=null) && (!consoleId.isEmpty())) {
            switch (consoleType)
            {
                case "PS3" :
                case "PS4" :
                    consoleIdText.setText("PLAYSTATION ID");
                    break;
                case "XBOXONE":
                case "XBOX360":
                    consoleIdText.setText("XBOX GAMERTAG");
                    break;

            }
        }
    }

//    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
//    {
//        int start = strBuilder.getSpanStart(span);
//        int end = strBuilder.getSpanEnd(span);
//        int flags = strBuilder.getSpanFlags(span);
//        ClickableSpan clickable = new ClickableSpan() {
//            public void onClick(View view) {
//                // Do something with span.getURL() to handle the link click...
//                webView.setVisibility(View.VISIBLE);
//                webView.loadUrl(span.getURL());
//            }
//        };
//        strBuilder.setSpan(clickable, start, end, flags);
//        strBuilder.removeSpan(span);
//    }

//    protected void setTextViewHTML(TextView text, String html)
//    {
//        CharSequence sequence = Html.fromHtml(html);
//        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
//        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
//        for(URLSpan span : urls) {
//            makeLinkClickable(strBuilder, span);
//        }
//        text.setText(strBuilder);
//        text.setMovementMethod(LinkMovementMethod.getInstance());
//    }

    public void showError(String err) {
        dialog.dismiss();
        signup_btn.setEnabled(true);
        setErrText(err);
    }

    private void enableSubmitIfReady() {
        if(name_signup!=null && pswd_signup!=null) {
            if (!name_signup.getText().toString().isEmpty() && !pswd_signup.getText().toString().isEmpty() && !psnid_signup.getText().toString().isEmpty()) {
                signup_btn.setImageDrawable(getResources().getDrawable(R.drawable.img_login_btn_tapped));
            } else {
                signup_btn.setImageDrawable(getResources().getDrawable(R.drawable.img_login_btn));
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        //dismiss dialog
        dialog.dismiss();
        if(data!=null) {
            UserData ud = (UserData) data;

            if (ud != null && ud.getUserId()!=null) {
                if(ud.getAuthenticationId() == Constants.REGISTER) {
                //save in preferrence
                Util.clearDefaults(getApplicationContext());
                Util.setDefaults("user", username, getApplicationContext());
                Util.setDefaults("password", password, getApplicationContext());

                mManager.setUserdata(ud);

                // decide the activity to open
                Intent regIntent = mManager.decideToOpenActivity(null);
//            Intent regIntent = new Intent(getApplicationContext(),
//                    CreateNewEvent.class);
                //regIntent.putExtra("userdata", ud);
                startActivity(regIntent);
                finish();
            }
        }
        }
    }

    @Override
    public void onBackPressed() {
        Util.clearDefaults(getApplicationContext());
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
