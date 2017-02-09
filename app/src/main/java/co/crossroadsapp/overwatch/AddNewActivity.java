package co.crossroadsapp.overwatch;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;

import java.util.Observable;
import java.util.Observer;

import co.crossroadsapp.overwatch.data.UserData;

public class AddNewActivity extends BaseActivity implements Observer {

    private ControlManager mCntrlMngr;
    private UserData user;
    private ImageView back;
    private ImageView exoticAct;
    private ImageView questAct;
    private ImageView storyAct;
    private ImageView patrolAct;
    private ImageView strikeAct;
    private ImageView crucibleAct;
    private ImageView arenaAct;
    private ImageView raiddAct;
    private ImageView featuredAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        setTRansparentStatusBar();

        mCntrlMngr = ControlManager.getmInstance();
        mCntrlMngr.setCurrentActivity(this);
        //mCntrlMngr.getAllActivities(this);
        boolean ads = false;
        String adP = null;

        Bundle b = getIntent().getExtras();
        //user = b.getParcelable("userdata");
        if (b != null) {
            if (b.containsKey("adcard")) {
                ads = b.getBoolean("adcard");
            }
            if (b.containsKey("adCardId")) {
                adP = b.getString("adCardId");
            }
        }

        user = mCntrlMngr.getUserData();

        checkIfAdFlow(ads, adP);

        back = (ImageView) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUpGameModeButtons();
    }

    private void setUpGameModeButtons() {
        View quick_play_btn = findViewById(R.id.quick_play_btn);
        if (quick_play_btn != null) {
            quick_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPostGetActivity("Quick Play");
                }
            });
        }

        View arcade_btn = findViewById(R.id.arcade_btn);
        if( arcade_btn != null )
        {
            arcade_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPostGetActivity("Arcade");
                }
            });
        }

        View competitive_play_btn = findViewById(R.id.competitive_play_btn);
        if( competitive_play_btn != null ) {
            competitive_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPostGetActivity("Competitive Play");
                }
            });
        }

        View play_vs_al_btn = findViewById(R.id.play_vs_al_btn);
        if (play_vs_al_btn != null) {
            play_vs_al_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPostGetActivity("Play vs. AI");
                }
            });
        }

        View custom_game_btn = findViewById(R.id.custom_game_btn);
        if (custom_game_btn != null) {
            custom_game_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPostGetActivity("Custom Game");
                }
            });
        }
    }

    private void callPostGetActivity(String type) {
        showProgressBar();
        RequestParams rp = new RequestParams();
        rp.add("aType", type);
        rp.add("includeTags", "true");
        mCntrlMngr.postGetActivityList(AddNewActivity.this, rp);
    }

    private void checkIfAdFlow(boolean ads, String adP) {
        if (ads) {
            if (adP != null) {
                //start new activity for add event creation
                Intent regIntent = new Intent(AddNewActivity.this,
                        AddFinalActivity.class);
                //regIntent.putExtra("userdata", user);
                regIntent.putExtra("adcard", ads);
                regIntent.putExtra("adCardId", adP);
                startActivity(regIntent);
            }
        }
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
        hideProgressBar();
        setErrText(err);
    }


    @Override
    public void update(Observable observable, Object data) {
        hideProgressBar();
        if (mCntrlMngr.getCurrentActivityList() != null && !mCntrlMngr.getCurrentActivityList().isEmpty()) {
            //start new activity for add event creation
            Intent regIntent = new Intent(AddNewActivity.this,
                    AddFinalActivity.class);
            //regIntent.putExtra("userdata", user);
            startActivity(regIntent);
        }
    }

    private void launchListActivityAndFinish() {
        Intent i = new Intent(this, ListActivityFragment.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //setButtonDefaultStates();
    }

    @Override
    public void onBackPressed() {
        launchListActivityAndFinish();
    }
}
