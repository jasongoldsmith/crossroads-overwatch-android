package co.crossroadsapp.overwatch;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.Observable;
import java.util.Observer;


public class ForgotLoginActivity extends BaseActivity implements Observer {

    private ImageView backBtn;
    private EditText psnId;
    private TextView resetPassword;
    private String console="PS4";

//    private RelativeLayout errLayout;
//    private TextView errText;
//    private ImageView close_err;
    private ProgressDialog dialog;
    private ControlManager mManager;
    private Spinner consoleType;
    private TextView psnId_text;
    private ImageView back;
    private CardView playstationBtn;
    private CardView xboxBtn;
    private TextView playstationBtnText;
    private TextView xboxBtnText;
    private ImageView heroImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_login);

        getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.img_background_signin));

        //setTRansparentStatusBar();
        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });

        dialog = new ProgressDialog(this);

        resetPassword = (TextView) findViewById(R.id.reset_pswd);

        psnId = (EditText) findViewById(R.id.forgot_psn);

        heroImg = (ImageView) findViewById(R.id.hero_img);

        psnId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    // the user is done typing.
                    resetPassword.performClick();
                }
                return false;
            }
        });

        playstationBtn = (CardView) findViewById(R.id.playstation_btn);
        xboxBtn = (CardView) findViewById(R.id.xbox_btn);
        playstationBtnText = (TextView) findViewById(R.id.playstation_text);
        xboxBtnText = (TextView) findViewById(R.id.xbox_text);

        playstationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playstationBtn.setCardBackgroundColor(getResources().getColor(R.color.app_theme_color));
                playstationBtnText.setTextColor(getResources().getColor(R.color.trimble_white));
                psnId.setHint(getResources().getString(R.string.playstation_hint));
                xboxBtn.setCardBackgroundColor(getResources().getColor(R.color.edittext_background));
                xboxBtnText.setTextColor(getResources().getColor(R.color.hinttext_color));
                console = "PS4";
            }
        });

        xboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xboxBtn.setCardBackgroundColor(getResources().getColor(R.color.app_theme_color));
                xboxBtnText.setTextColor(getResources().getColor(R.color.trimble_white));
                psnId.setHint(getResources().getString(R.string.xbox_hint));
                playstationBtn.setCardBackgroundColor(getResources().getColor(R.color.edittext_background));
                playstationBtnText.setTextColor(getResources().getColor(R.color.hinttext_color));
                console = "XBOXONE";
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (psnId!=null && console!=null) {
                    String psnString = psnId.getText().toString();
                    if (!psnString.isEmpty()) {
                        dialog.show();
                        resetPassword.setEnabled(false);
                        RequestParams params = new RequestParams();
                        //params.put("userName", psnString);
                        params.add("consoleId", psnString);
                        params.add("consoleType", console);
                        mManager.postResetPassword(ForgotLoginActivity.this, params);
                    } else {
                        showError(getResources().getString(R.string.username_missing));
                    }
                }
            }
        });

        final View contentView = this.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    heroImg.setVisibility(View.GONE);
                }
                else {
                    // keyboard is closed
                    heroImg.setVisibility(View.VISIBLE);
                    //finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showKeyboard();
//            }
//
//        }, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
    }

    private void showKeyboard() {
        psnId.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void hideKeyboard() {
        psnId.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ForgotLoginActivity.this.getCurrentFocus().getWindowToken(),0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTRansparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void showError(String err) {
        dialog.dismiss();
        resetPassword.setEnabled(true);
        setErrText(err);
    }

    @Override
    public void update(Observable observable, Object data) {
        dialog.dismiss();
        psnId.setText("");
        Intent intent = new Intent(getApplicationContext(),
                PasswordReset.class);
        startActivity(intent);
        finish();
//        Toast.makeText(this, "Instructions for resetting your password have been sent to your Bungie.net account. Follow the instructions to choose a new password.",
//                Toast.LENGTH_LONG).show();
//        long timeInMillisecondTheToastIsShowingFor = 3000;
//        (new Handler())
//                .postDelayed(
//                        new Runnable() {
//                            public void run() {
//                                // finish this activity here
//                                finish();
//                            }
//                        }, timeInMillisecondTheToastIsShowingFor);
    }

}
