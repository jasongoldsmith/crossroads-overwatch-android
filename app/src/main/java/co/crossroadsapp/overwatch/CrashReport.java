package co.crossroadsapp.overwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sharmha on 3/31/16.
 */
public class CrashReport extends BaseActivity implements Observer {

    private EditText crash_text;
    private TextView send_crash;
    private UserData user;
    private ControlManager controlManager;
    private ImageView backBtn;
    private EditText email;
    private boolean reportIssue=false;
    private RequestParams requestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_report_activity);

        getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_background_signin));

        controlManager = ControlManager.getmInstance();

        requestParams = new RequestParams();

        Bundle b = getIntent().getExtras();
        if(b!=null && b.containsKey("reportIssue")) {
            reportIssue = true;
            requestParams = (RequestParams) b.get("requestParams");
        }
        user = controlManager.getUserData();

        controlManager.setCurrentActivity(CrashReport.this);

        crash_text = (EditText) findViewById(R.id.crash_edittext);
        email = (EditText) findViewById(R.id.crash_email);
        send_crash = (TextView) findViewById(R.id.send_crash);
        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView crash_text_header = (TextView) findViewById(R.id.crash_text);

        if(user!=null && user.getEmail()!=null) {
            email.setText(user.getEmail());
            email.setKeyListener(null);
        }

        if(reportIssue) {
            send_crash.setText("SUBMIT");
            crash_text_header.setText(getResources().getString(R.string.report_issue_header));
            crash_text.setHint("  Description (required)");
        }

//        crash_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
//                    // the user is done typing.
//                    send_crash.requestFocus();
//                    send_crash.performClick();
//                }
//                return false;
//            }
//        });

        send_crash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(crash_text.getText().toString().length()>0 && !email.getText().toString().isEmpty()){
                    //send crash report
                    hideKeyboard();
                    showProgressBar();
                    if(Util.isValidEmail(email.getText().toString())) {
                        send_crash.setEnabled(false);
                        if(reportIssue) {
                            Map<String, String> json = new HashMap<String, String>();
                            json.put("email", email.getText().toString());
                            json.put("description", crash_text.getText().toString());
                            requestParams.put("formDetails", json);
                        } else {
                            if (user != null && user.getUserId() != null) {
                                //requestParams.put("reporter", user.getUserId());
                            }
                            requestParams.put("email", email.getText().toString());
                            requestParams.put("description", crash_text.getText().toString());
                        }
                        HashMap<String, Integer> sourceValue = new HashMap<>();
                        sourceValue.put("sourceCode", Constants.INAPP);
                        requestParams.put("source", sourceValue);
                        controlManager.postCrash(CrashReport.this, requestParams, reportIssue? Constants.REPORT_COMMENT_NEXT:Constants.GENERAL_ERROR);
                        //showToastAndClose();
                    } else {
                        showError(getString(R.string.invalid_email));
                    }
                }
            }
        });
    }

    public void showError(String err) {
        hideProgressBar();
        send_crash.setEnabled(true);
        //setErrText(err);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(CrashReport.this.getCurrentFocus().getWindowToken(),0);
    }

    private void showToastAndClose() {
        Toast.makeText(this, "Message Sent",
                Toast.LENGTH_SHORT).show();
        long timeInMillisecondTheToastIsShowingFor = 1000;
        (new Handler())
                .postDelayed(
                        new Runnable() {
                            public void run() {
                                // finish this activity here
                                finish();
                            }
                        }, timeInMillisecondTheToastIsShowingFor);
    }

    @Override
    public void update(Observable observable, Object data) {
        hideProgressBar();
        send_crash.setEnabled(true);
        if(data!=null && data instanceof LoginError) {
            setErrText((LoginError) data);
        } else {
            if (reportIssue) {
                showGenericError(getString(R.string.report_submitted_header), getString(R.string.report_submitted), "OK", null, Constants.GENERAL_ERROR, null, false);
            } else {
                Intent intent = new Intent(getApplicationContext(),
                        MessageSent.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
