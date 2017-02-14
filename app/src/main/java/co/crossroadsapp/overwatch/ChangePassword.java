package co.crossroadsapp.overwatch;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.Observable;
import java.util.Observer;

public class ChangePassword extends BaseActivity implements Observer {

    private ImageView backBtn;
    private EditText oldPswrd;
    private TextView setPswrd;
    private EditText newPswrd;
    private ControlManager mManager;
    private ProgressDialog dialog;
    String userId;
    String newP;
    private ImageView showPswrdOld;
    private ImageView showPswrdCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setPswrd = (TextView) findViewById(R.id.send_change_pswrd);

        oldPswrd = (EditText) findViewById(R.id.pswrd_edit);
        newPswrd = (EditText) findViewById(R.id.pswrd_edit_new);

        //hint configuration
        oldPswrd.setTypeface(Typeface.DEFAULT);
        oldPswrd.setTransformationMethod(new PasswordTransformationMethod());
        newPswrd.setTypeface(Typeface.DEFAULT);
        newPswrd.setTransformationMethod(new PasswordTransformationMethod());

        showPswrdOld = (ImageView) findViewById(R.id.show_pswd_old);

        final boolean[] showPswdState = {false};

        showPswrdOld.setOnClickListener(new View.OnClickListener() {
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

        showPswrdCurrent = (ImageView) findViewById(R.id.show_pswd_new);

        final boolean[] showPswdState1 = {false};

        showPswrdCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tracking showPassword click
//                Map<String, String> json = new HashMap<String, String>();
//                Util.postTracking(json, LoginActivity.this, mManager, Constants.APP_SHOWPASSWORD);
                if(newPswrd!=null && !newPswrd.getText().toString().isEmpty()) {
                    if(!showPswdState1[0]) {
                        newPswrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        newPswrd.setSelection(newPswrd.getText().length());
                        newPswrd.setTypeface(Typeface.DEFAULT);
                        showPswdState1[0] = true;
                    } else {
                        newPswrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        newPswrd.setSelection(oldPswrd.getText().length());
                        newPswrd.setTypeface(Typeface.DEFAULT);
                        showPswdState1[0] = false;
                    }
                }
            }
        });

        newPswrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        dialog = new ProgressDialog(this);

        if(mManager!=null && mManager.getUserData()!=null) {
            UserData ud = mManager.getUserData();
            userId = ud.getUserId();
        }

        setPswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (userId != null) {
                    v.setEnabled(false);
                    String oldP = oldPswrd.getText().toString();
                    newP = newPswrd.getText().toString();
                    if (oldP != null && newP != null) {
                        if (newP.length() > 4 && oldP.length() > 4) {
                            RequestParams params = new RequestParams();
                            params.put("oldPassword", oldP);
                            params.put("newPassword", newP);
                            //params.put("id", userId);
                            showProgressBar();
                            mManager.postChangePassword(ChangePassword.this, params);
                        } else {
                            showError(getResources().getString(R.string.password_short));
                        }
                    }
//                }
            }
        });
    }

    public void showError(String err) {
        hideProgressBar();
        setPswrd.setEnabled(true);
        //setErrText(err);
    }

    @Override
    public void update(Observable observable, Object data) {
        hideProgressBar();
        setPswrd.setEnabled(true);
        if(data!=null) {
            if(data instanceof LoginError) {
                setErrText((LoginError) data);
            }
        } else {
            if (newP != null && (!newP.isEmpty())) {
                Util.setDefaults("password", newP, getApplicationContext());
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
