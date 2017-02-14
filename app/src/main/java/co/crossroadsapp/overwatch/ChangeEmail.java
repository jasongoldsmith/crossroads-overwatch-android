package co.crossroadsapp.overwatch;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Util;

public class ChangeEmail extends BaseActivity implements Observer {

    private ImageView backBtn;
    private EditText oldPswrd;
    private ControlManager mManager;
    String userId;
    String newE;
    private TextView oldEmail;
    private EditText newEmail;
    private TextView setPswrd;
    private ImageView showPswrdCurrent;
    private UserData ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setPswrd = (TextView) findViewById(R.id.send_change_email);

        oldPswrd = (EditText) findViewById(R.id.pswrd_edit);
        oldEmail = (TextView) findViewById(R.id.current_email);
        newEmail = (EditText) findViewById(R.id.email_edit_new);

        //hint configuration
        oldPswrd.setTypeface(Typeface.DEFAULT);
        oldPswrd.setTransformationMethod(new PasswordTransformationMethod());

        showPswrdCurrent = (ImageView) findViewById(R.id.show_pswd_old);

        final boolean[] showPswdState = {false};

        showPswrdCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tracking showPassword click
//                Map<String, String> json = new HashMap<String, String>();
//                Util.postTracking(json, LoginActivity.this, mManager, Constants.APP_SHOWPASSWORD);
                if(oldPswrd!=null && !oldPswrd.getText().toString().isEmpty()) {
                    if(!showPswdState[0]) {
                        oldPswrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        oldPswrd.setSelection(oldPswrd.getText().length());
                        oldPswrd.setTypeface(Typeface.DEFAULT);
                        showPswdState[0] = true;
                    } else {
                        oldPswrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        oldPswrd.setSelection(oldPswrd.getText().length());
                        oldPswrd.setTypeface(Typeface.DEFAULT);
                        showPswdState[0] = false;
                    }
                }
            }
        });

        oldPswrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    // the user is done typing.
                    setPswrd.requestFocus();
                    setPswrd.performClick();
                }
                return false;
            }
        });

        if (mManager != null && mManager.getUserData() != null) {
            ud = mManager.getUserData();
            userId = ud.getUserId();
        }

        if(ud!=null && ud.getEmail()!=null) {
            oldEmail.setText(ud.getEmail());
            oldEmail.setKeyListener(null);
        }

        setPswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (userId != null) {
                    v.setEnabled(false);
                    String oldP = oldPswrd.getText().toString();
                    String oldE = oldEmail.getText().toString();
                    //newP = newPswrd.getText().toString();
                    newE = newEmail.getText().toString();
                    if ((oldP != null && newE != null)) {
                        if (!oldP.isEmpty() && !newE.isEmpty()) {
                            RequestParams params = new RequestParams();
                            params.put("password", oldP);
                            params.put("newEmail", newE);
                            showProgressBar();
                            mManager.postChangeEmail(ChangeEmail.this, params);
                        } else {
                            showError("Please enter your email/password");
                        }
                    }
//                }
            }
        });
    }

    public void showError(String err) {
        //dialog.dismiss();
        hideProgressBar();
        setPswrd.setEnabled(true);
        //setErrText(err);
    }

    @Override
    public void update(Observable observable, Object data) {
        //dialog.dismiss();
        hideProgressBar();
        if(data!=null) {
            if(data instanceof LoginError) {
                if(((LoginError) data).getGeneralServerError().getErrorDetails().getMessage()!=null) {
                    setErrText((LoginError) data);
                }
            }
        } else {
            if (newE != null && (!newE.isEmpty())) {
                Util.setDefaults("user", newE, getApplicationContext());
            }
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
