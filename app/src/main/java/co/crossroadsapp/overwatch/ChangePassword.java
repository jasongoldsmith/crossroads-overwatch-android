package co.crossroadsapp.overwatch;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.Observable;
import java.util.Observer;

public class ChangePassword extends BaseActivity implements Observer {

    private ImageView backBtn;
    private EditText oldPswrd;
    private CardView setPswrd;
    private EditText newPswrd;
    private ControlManager mManager;
    private ProgressDialog dialog;
    String userId;
    String newP;

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

        setPswrd = (CardView) findViewById(R.id.send_change_pswrd);

        oldPswrd = (EditText) findViewById(R.id.pswrd_edit);
        newPswrd = (EditText) findViewById(R.id.pswrd_edit_new);

        //hint configuration
        oldPswrd.setTypeface(Typeface.DEFAULT);
        oldPswrd.setTransformationMethod(new PasswordTransformationMethod());
        newPswrd.setTypeface(Typeface.DEFAULT);
        newPswrd.setTransformationMethod(new PasswordTransformationMethod());

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
                if (userId != null) {
                    v.setEnabled(false);
                    String oldP = oldPswrd.getText().toString();
                    newP = newPswrd.getText().toString();
                    if (oldP != null && newP != null) {
                        if (newP.length() > 4 && oldP.length() > 4) {
                            RequestParams params = new RequestParams();
                            params.put("oldPassWord", oldP);
                            params.put("newPassWord", newP);
                            params.put("id", userId);
                            dialog.show();
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            mManager.postChangePassword(ChangePassword.this, params);
                        } else {
                            showError(getResources().getString(R.string.password_short));
                        }
                    }
                }
            }
        });
    }

    public void showError(String err) {
        dialog.dismiss();
        setPswrd.setEnabled(true);
        setErrText(err);
    }

    @Override
    public void update(Observable observable, Object data) {
        dialog.dismiss();
        if(newP!=null && (!newP.isEmpty())) {
            Util.setDefaults("password", newP, getApplicationContext());
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
