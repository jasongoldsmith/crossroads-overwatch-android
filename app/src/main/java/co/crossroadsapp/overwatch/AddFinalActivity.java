package co.crossroadsapp.overwatch;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import co.crossroadsapp.overwatch.data.ActivityData;
import co.crossroadsapp.overwatch.data.CurrentEventDataHolder;
import co.crossroadsapp.overwatch.data.EventData;
import co.crossroadsapp.overwatch.data.LoginError;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;

public class AddFinalActivity extends BaseActivity implements Observer, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    private ControlManager mCntrlMngr;
    private UserData user;
    private ImageView back;
    ImageView actIcon;
    ImageView background;
    TextView modifier;
    TextView actSubtype;
    TextView actSubtypeDropdownText;
    TextView checkpointText;
    TextView detailText;
    TextView levelTextView;
    private RelativeLayout checkpointLayout;
    private RelativeLayout subtypeLayout;
    private RelativeLayout detailLayout;
    private ArrayList<ActivityData> activity;
    private ArrayList<ActivityData> checkpointActList;
    private ArrayList<String> checkpointItems;
    private ArrayAdapter<String> adapterCheckpoint;
    private Spinner dropdownCheckpoint;
    private ArrayList<ActivityData> actSubList;
    private ArrayList<String> actSubTypeList;
    private Spinner dropdownSubtype;

    private RelativeLayout date;
    private TextView date_display;
    private RelativeLayout time;
    private TextView time_display;

    static final int TIME_DIALOG_ID = 999;
    static final int DATE_DIALOG_ID = 888;
    private int hour;
    private int minute;
    private DatePickerDialog mDatePickerDai;
    private TimePickerDialog tpd;
    private ArrayList<String> tagList;
    private String activityType;
    private String subActType;
    private String subtypeDifficulty;
    private Spinner dropdownDetails;
    private ArrayList<ActivityData> tagActList;
    private ActivityData finalAct;
    private TextView createNewEventBtn;
    private ArrayAdapter<String> adapterTags;
    private ArrayAdapter<String> adapterSubtypes;
    private ImageView tagDropdownArw;
    private ImageView checkpointDropdownArw;
    private ImageView subtypeDropdownArw;
    private TextView lightTextView;
    private RelativeLayout modifiersLayout;
    private RelativeLayout modifiersLayout2;
    private RelativeLayout modifiersLayout3;
    private RelativeLayout levelLayout;

    TimePicker timePicker;
    private int TIME_PICKER_INTERVAL = 15;
    NumberPicker minutePicker;
    List<String> displayedValues;

    boolean ads = false;
    String adP = null;
    boolean adActivity;
    private ActivityData ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_final);

        setTRansparentStatusBar();
        mCntrlMngr = ControlManager.getmInstance();
        mCntrlMngr.setCurrentActivity(this);
        //mCntrlMngr.getAllActivities(this);

        Bundle b = getIntent().getExtras();
        //user = b.getParcelable("userdata");

        user = mCntrlMngr.getUserData();

        if (b != null) {
            ads = b.getBoolean("adcard");
            adP = b.getString("adCardId");
            adActivity = ads;
        }

        back = (ImageView) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        actIcon = (ImageView) findViewById(R.id.event_icon1);

        background = (ImageView) findViewById(R.id.background);

        actSubtype = (TextView) findViewById(R.id.act_subtype);

        levelTextView = (TextView) findViewById(R.id.level_text);

        levelLayout = (RelativeLayout) findViewById(R.id.level_layout);

        lightTextView = (TextView) findViewById(R.id.light);

        actSubtypeDropdownText = (TextView) findViewById(R.id.act_subtype_text);

        subtypeLayout = (RelativeLayout) findViewById(R.id.final_create_event_firstcard);
        detailLayout = (RelativeLayout) findViewById(R.id.event_creation_detail_layout);

        activity = mCntrlMngr.getCurrentActivityList();

        //modifier = (TextView) findViewById(R.id.modifier);

        checkpointText = (TextView) findViewById(R.id.ad_checkpoint_text);

        checkpointLayout = (RelativeLayout) findViewById(R.id.event_creation_checkpoint_layout);

        modifiersLayout = (RelativeLayout) findViewById(R.id.modifier_layout);
        modifiersLayout2 = (RelativeLayout) findViewById(R.id.modifier_layout2);
        modifiersLayout3 = (RelativeLayout) findViewById(R.id.modifier_layout3);

        detailText = (TextView) findViewById(R.id.ad_detail_text);
        subtypeDropdownArw = (ImageView) findViewById(R.id.subtype_downarw);
        checkpointDropdownArw = (ImageView) findViewById(R.id.checkpoint_downarw);
        tagDropdownArw = (ImageView) findViewById(R.id.tag_downarw);

        //checkpoint dropdown
        dropdownCheckpoint = (Spinner) findViewById(R.id.event_creation_checkpoint);

        checkpointItems = new ArrayList<String>();
        dropdownCheckpoint.setOnItemSelectedListener(AddFinalActivity.this);
        dropdownCheckpoint.setOnTouchListener(this);

        //activitySubtype dropdown
        dropdownSubtype = (Spinner) findViewById(R.id.event_creation_subtype);

        actSubTypeList = new ArrayList<String>();
        dropdownSubtype.setOnItemSelectedListener(AddFinalActivity.this);
        dropdownSubtype.setOnTouchListener(this);

        //details
        dropdownDetails = (Spinner) findViewById(R.id.event_creation_detail);
        tagList = new ArrayList<String>();
        dropdownDetails.setOnItemSelectedListener(AddFinalActivity.this);
        dropdownDetails.setOnTouchListener(this);

        refreshActivityUI();

        createAds();

        date = (RelativeLayout) findViewById(R.id.date);
        date_display = (TextView) findViewById(R.id.date_text);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        time = (RelativeLayout) findViewById(R.id.time);
        time_display = (TextView) findViewById(R.id.time_text);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        createNewEventBtn = (TextView) findViewById(R.id.create_new_event_activity);

        createNewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalAct != null && user != null) {
                    hideProgressBar();
                    showProgressBar();
                    String dateTime = getCreateEventDateTime();
                    if (finalAct.getId() != null && user.getUserId() != null) {
                        mCntrlMngr.postCreateEvent(finalAct.getId(), user.getUserId(), finalAct.getMinPlayer(), finalAct.getMaxPlayer(), dateTime, AddFinalActivity.this);
                    }
                }
            }
        });
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

    private void createAds() {
        if (ads) {
            ad = mCntrlMngr.getAdsActivity(adP);
            if (ad != null) {
                String subtypeDiff = ad.getActivitySubtype();
                if (subtypeDiff != null) {
                    subActType = subtypeDiff;
                }
                if (ad.getActivityDifficulty() != null && !ad.getActivityDifficulty().isEmpty()) {
                    subtypeDifficulty = ad.getActivityDifficulty();
                }
                if (!ad.getActivityDifficulty().isEmpty()) {
                    subtypeDiff = subtypeDiff + " - " + ad.getActivityDifficulty();
                }
                dropdownSubtype.setSelection(actSubTypeList.indexOf(subtypeDiff));
                if (!ad.getActivityCheckpoint().isEmpty()) {
                    if (!checkpointItems.isEmpty()) {
                        dropdownCheckpoint.setSelection(checkpointItems.indexOf(ad.getActivityCheckpoint()));
                    }
                }
                if (!ad.getTag().isEmpty()) {
                    dropdownDetails.setSelection(tagList.indexOf(ad.getTag()));
                }
            }
        }
    }

    private void refreshActivityUI() {
        String actIconUrl = null;
        String backg = null;
        String subType = null;
        String aType = null;
        int level = 0;
        int light = 0;
        String description = null;
        String levelText = null;
        String lightText = null;
        String subtypeDiff = null;
        String check = null;
        String localTag = null;
        String location = null;
        String next = null;
        ArrayList<String> modi = new ArrayList<String>();
        ArrayList<String> bonus = new ArrayList<String>();

        if (finalAct != null && !finalAct.getActivitySubtype().isEmpty()) {
            actIconUrl = finalAct.getActivityIconUrl();
            backg = finalAct.getaImagePath();
            activityType = finalAct.getActivityType();
            subType = finalAct.getActivitySubtype();
            aType = finalAct.getActivityType();
            level = finalAct.getActivityLevel();
            light = finalAct.getActivityLight();
            subtypeDiff = finalAct.getActivitySubtype();
            if (!finalAct.getActivityDifficulty().isEmpty()) {
                subtypeDiff = subtypeDiff + " - " + finalAct.getActivityDifficulty();
            }
            check = finalAct.getActivityCheckpoint();
            localTag = finalAct.getTag();
            description = finalAct.getaDescription();
            location = finalAct.getaLocation();
            if (finalAct.getModifierList() != null && !finalAct.getModifierList().isEmpty()) {
                for (int y = 0; y < finalAct.getModifierList().size(); y++) {
                    modi.add(finalAct.getModifierList().get(y).getaModifierName());
                }
            }
            if (finalAct.getBonusList() != null && !finalAct.getBonusList().isEmpty()) {
                for (int y = 0; y < finalAct.getBonusList().size(); y++) {
                    bonus.add(finalAct.getBonusList().get(y).getaBonusName());
                }
            }
        } else {
            if (activity != null && activity.get(0) != null) {
                actIconUrl = activity.get(0).getActivityIconUrl() != null ? activity.get(0).getActivityIconUrl() : null;
                backg = activity.get(0).getaImagePath() != null ? activity.get(0).getaImagePath() : null;
                subType = activity.get(0).getActivitySubtype() != null ? activity.get(0).getActivitySubtype() : "";
                level = activity.get(0).getActivityLevel();
                check = activity.get(0).getActivityCheckpoint();
                light = activity.get(0).getActivityLight();
                description = activity.get(0).getaDescription() != null ? activity.get(0).getaDescription() : "";
                location = activity.get(0).getaLocation() != null ? activity.get(0).getaLocation() : "";
                if (activity.get(0).getModifierList() != null && !activity.get(0).getModifierList().isEmpty()) {
                    for (int y = 0; y < activity.get(0).getModifierList().size(); y++) {
                        modi.add(activity.get(0).getModifierList().get(y).getaModifierName());
                    }
                }
                if (activity.get(0).getBonusList() != null && !activity.get(0).getBonusList().isEmpty()) {
                    for (int y = 0; y < activity.get(0).getBonusList().size(); y++) {
                        bonus.add(activity.get(0).getBonusList().get(y).getaBonusName());
                    }
                }
            }
        }

        lightTextView.setText("");

        if (level != 0) {
            levelText = "LEVEL " + level;
            if (light != 0) {
                levelText = levelText + "  Recommended Light: ";
                lightTextView.setText("\u2726" + light);
            } else {
                levelText = levelText + "  " + location;
            }
        } else {
            levelText = description;
        }

        if (!levelText.isEmpty()) {
            levelLayout.setVisibility(View.GONE);
            levelTextView.setText(levelText);
        } else {
            levelLayout.setVisibility(View.GONE);
        }

        //show modifiers and bonuses
        modifiersLayout.removeAllViews();
        modifiersLayout2.removeAllViews();
        modifiersLayout3.removeAllViews();
        modifiersLayout.setVisibility(View.GONE);
        modifiersLayout2.setVisibility(View.GONE);
        modifiersLayout3.setVisibility(View.GONE);
        Random rnd = new Random();
        int prevTextViewId = 0;
        int pad = Util.dpToPx(4, AddFinalActivity.this);
        int pad8 = Util.dpToPx(8, AddFinalActivity.this);
        int pad6 = Util.dpToPx(6, AddFinalActivity.this);
        ArrayList<String> completList = new ArrayList<String>();
        completList.addAll(modi);
        completList.addAll(bonus);
        if (modi.isEmpty()) {
            if (check != null && !check.isEmpty()) {
                completList.add(check);
            }
        }

        if (!completList.isEmpty()) {
            final TextView[] tv = new TextView[completList.size()];
            if (completList.size() < 11) {
                for (int i = 0; i < completList.size(); i++) {
                    tv[i] = new TextView(this);
                    tv[i].setText(completList.get(i));
                    tv[i].setVisibility(View.INVISIBLE);
                    tv[i].setTextColor(getResources().getColor(R.color.trimble_white));
                    tv[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tv[i].setAllCaps(true);
                    tv[i].setPadding(pad8, pad, pad8, pad);
                    GradientDrawable gd = new GradientDrawable();
                    gd.setCornerRadius(5);
                    gd.setStroke(2, 0xFFFFFFFF);
                    tv[i].setBackgroundDrawable(gd);

                    int curTextViewId = prevTextViewId + 1;
                    tv[i].setId(curTextViewId);
                    final RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);

                    params.addRule(RelativeLayout.RIGHT_OF, prevTextViewId);
                    params.leftMargin = pad6;
                    tv[i].setLayoutParams(params);

                    prevTextViewId = curTextViewId;
                    modifiersLayout3.setVisibility(View.VISIBLE);
                    modifiersLayout3.addView(tv[i], params);

//                    DisplayMetrics metrics = getResources().getDisplayMetrics();
//                    int width = metrics.widthPixels;
//                    int textwidth = textView.getWidth();
//                    int modiWidth = modifiersLayout3.getWidth();
//                    int padding = Util.dpToPx(70, this);
//                    int w = textView.getWidth()+modifiersLayout3.getWidth()+Util.dpToPx(70, this);
                    //if (textView.getWidth()+modifiersLayout3.getWidth()+Util.dpToPx(70, this) < metrics.widthPixels) {
//                    if (i < 2) {
//                        modifiersLayout3.setVisibility(View.VISIBLE);
//                        modifiersLayout3.addView(textView, params);
//                    } else if (textView.getWidth()+modifiersLayout2.getWidth()+Util.dpToPx(70, this) < metrics.widthPixels) {
//                        int n = modifiersLayout3.getWidth();
//                        modifiersLayout2.setVisibility(View.VISIBLE);
//                        modifiersLayout2.addView(textView, params);
//                    } else {
//                        modifiersLayout.setVisibility(View.VISIBLE);
//                        modifiersLayout.addView(textView, params);
//                    }
                }

                modifiersLayout3.post(new Runnable() {    //post a Runnable that call reLayout to layout object
                    @Override
                    public void run() {
                        int tvWidth = 0;
                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        int width = metrics.widthPixels - Util.dpToPx(90, AddFinalActivity.this);
                        modifiersLayout3.removeAllViews();
                        for (int i = 0; i < tv.length; i++) {
                            int n = tv[i].getWidth();
                            tvWidth = tvWidth + tv[i].getWidth();
                            if (tvWidth < width) {
                                modifiersLayout3.setVisibility(View.VISIBLE);
                                modifiersLayout3.addView(tv[i]);
                            } else {
                                modifiersLayout2.setVisibility(View.VISIBLE);
                                modifiersLayout2.addView(tv[i]);
                            }
                        }
                    }
                });
            }
        }

        //load activity icon
        Util.picassoLoadImageWithoutMeasurement(getApplicationContext(), actIcon, actIconUrl, R.mipmap.icon_notification);

        //load background image
        Util.picassoLoadImageWithoutMeasurement(getApplicationContext(), background, backg, R.mipmap.img_top_hero_c_p);

        actSubtype.setText(aType);
        actSubtype.setAllCaps(true);

        if (finalAct == null) {
            //subtypes and difficulty level
            //actSubList = mCntrlMngr.getCustomActivityList(activity.get(0).getActivityType());
            activityType = activity.get(0).getActivityType();
            actSubTypeList = new ArrayList<String>();
            if (activity != null && !activity.isEmpty()) {
                for (int n = 0; n < activity.size(); n++) {
                    String subtypeDifficultyName = "";
                    if (activity.get(n).getActivitySubtype() != null && !activity.get(n).getActivitySubtype().isEmpty()) {
                        subActType = activity.get(n).getActivitySubtype();
                        subtypeDifficultyName = subActType;
                        if (activity.get(n).getActivityDifficulty() != null && !activity.get(n).getActivityDifficulty().isEmpty()) {
                            subtypeDifficulty = activity.get(n).getActivityDifficulty();
                            subtypeDifficultyName = subtypeDifficultyName + " - " + subtypeDifficulty;
                        }
                    }
                    actSubTypeList.add(subtypeDifficultyName);
                }
            }

            actSubTypeList = Util.removeListDuplicates(actSubTypeList);
            sortList(actSubTypeList);

            updateDrawerSubtype(actSubTypeList);
        }
    }

    public ArrayList<String> concat(ArrayList<String> a, ArrayList<String> b) {
        int aLen = a.size();
        int bLen = b.size();
        ArrayList<String> c = new ArrayList<String>();
        a.addAll(b);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    private void sortList(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
    }

    private void checkpointSortList(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.compareToIgnoreCase(s1);
            }
        });
    }

    private void getTagList(String checkPoint) {
        String cp = "";
        if (checkpointItems != null && !checkpointItems.isEmpty()) {
            if (checkPoint != null) {
                cp = checkPoint;
            } else {
                cp = checkpointItems.get(0);
            }
        }
        tagList.clear();
        tagActList = new ArrayList<ActivityData>();
        if (checkpointActList != null && !checkpointActList.isEmpty()) {
            for (int i = 0; i < checkpointActList.size(); i++) {
                if (checkpointActList.get(i).getActivitySubtype() != null && !checkpointActList.get(i).getActivitySubtype().isEmpty()) {
                    //subActType = actSubList.get(n).getActivitySubtype();
                    if (checkpointActList.get(i).getActivityDifficulty() != null && !checkpointActList.get(i).getActivityDifficulty().isEmpty()) {
                        if (subActType.equalsIgnoreCase(checkpointActList.get(i).getActivitySubtype()) && subtypeDifficulty.equalsIgnoreCase(checkpointActList.get(i).getActivityDifficulty())) {
                            if (checkpointActList.get(i).getActivityCheckpoint() != null && !checkpointActList.get(i).getActivityCheckpoint().equalsIgnoreCase("null") && !checkpointActList.get(i).getActivityCheckpoint().isEmpty()) {
                                if (checkpointActList.get(i).getActivitySubtype().equalsIgnoreCase(subActType) && checkpointActList.get(i).getActivityDifficulty().equalsIgnoreCase(subtypeDifficulty)) {
                                    if (checkpointActList.get(i).getActivityCheckpoint().equalsIgnoreCase(cp)) {
                                        if (checkpointActList.get(i).getTag() != null) {
                                            if (!checkpointActList.get(i).getTag().isEmpty()) {
                                                tagList.add(checkpointActList.get(i).getTag());
                                            } else {
                                                tagList.add(Constants.NONE);
                                            }
                                            tagActList.add(checkpointActList.get(i));
                                        }
                                    }
                                }
                            } else {
                                if (checkpointActList.get(i).getActivitySubtype().equalsIgnoreCase(subActType)) {
                                    if (checkpointActList.get(i).getTag() != null) {
                                        if (!checkpointActList.get(i).getTag().isEmpty()) {
                                            tagList.add(checkpointActList.get(i).getTag());
                                        } else {
                                            tagList.add(Constants.NONE);
                                        }
                                        tagActList.add(checkpointActList.get(i));
                                    }
                                }
                            }
                        }
                    } else {
                        if (checkpointActList.get(i).getActivityCheckpoint() != null && !checkpointActList.get(i).getActivityCheckpoint().equalsIgnoreCase("null") && !checkpointActList.get(i).getActivityCheckpoint().isEmpty()) {
                            if (checkpointActList.get(i).getActivitySubtype().equalsIgnoreCase(subActType)) {
                                if (checkpointActList.get(i).getActivityCheckpoint().equalsIgnoreCase(cp)) {
                                    if (checkpointActList.get(i).getTag() != null) {
                                        if (!checkpointActList.get(i).getTag().isEmpty()) {
                                            tagList.add(checkpointActList.get(i).getTag());
                                        } else {
                                            tagList.add(Constants.NONE);
                                        }
                                        tagActList.add(checkpointActList.get(i));
                                    }
                                }
                            }
                        } else {
                            if (checkpointActList.get(i).getActivitySubtype().equalsIgnoreCase(subActType)) {
                                if (checkpointActList.get(i).getTag() != null) {
                                    if (!checkpointActList.get(i).getTag().isEmpty()) {
                                        tagList.add(checkpointActList.get(i).getTag());
                                    } else {
                                        tagList.add(Constants.NONE);
                                    }
                                    tagActList.add(checkpointActList.get(i));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (tagList.isEmpty()) {
            tagList.add(Constants.NONE);
        }
        updateDrawerTags(tagList);
    }

    private void getCheckpointList() {
        //checkpoints
        if (checkpointActList == null) {
            checkpointActList = new ArrayList<ActivityData>();
        } else {
            checkpointActList.clear();
        }
        checkpointActList = mCntrlMngr.getCheckpointActivityList(subActType, subtypeDifficulty);

        checkpointItems.clear();

        for (int i = 0; i < checkpointActList.size(); i++) {
            if (checkpointActList.get(i).getActivityCheckpoint() != null && !checkpointActList.get(i).getActivityCheckpoint().equalsIgnoreCase("null") && !checkpointActList.get(i).getActivityCheckpoint().isEmpty()) {
                checkpointItems.add(checkpointActList.get(i).getActivityCheckpoint());
            }
        }

        //remove duplicates
        checkpointItems = Util.removeListDuplicates(checkpointItems);
        checkpointSortList(checkpointItems);

        updateDrawer(checkpointItems);

        getTagList(null);
    }

    private void updateDrawer(final ArrayList<String> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            if (checkpointLayout != null) {
                checkpointLayout.setVisibility(View.VISIBLE);
            }
            if (dataList.size() > 1) {
                checkpointDropdownArw.setVisibility(View.VISIBLE);
                adapterCheckpoint = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList) {

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        checkpointText.setText(((TextView) v).getText());
                        ((TextView) v).setVisibility(View.GONE);
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        return getCheckpointCustomView(position, convertView, parent, dataList);
                    }
                };
                adapterCheckpoint.setDropDownViewResource(R.layout.empty_layout);
                dropdownCheckpoint.setEnabled(true);
                dropdownCheckpoint.setAdapter(adapterCheckpoint);
                adapterCheckpoint.notifyDataSetChanged();
            } else {
                dropdownCheckpoint.setEnabled(false);
                checkpointText.setText(dataList.get(0).toString());
                checkpointDropdownArw.setVisibility(View.GONE);
            }
        } else {
            //todo checkpoint layout visibility gone
            if (checkpointLayout != null) {
                checkpointLayout.setVisibility(View.GONE);
            }
        }
    }

    private void updateDrawerTags(final ArrayList<String> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.size() > 1) {
                tagDropdownArw.setVisibility(View.VISIBLE);
                adapterTags = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        if (((TextView) v).getText().toString().equalsIgnoreCase(Constants.NONE)) {
                            detailText.setText("Details (Optional)");
                        } else {
                            detailText.setText(((TextView) v).getText());
                        }
                        ((TextView) v).setVisibility(View.GONE);
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        return getTagCustomView(position, convertView, parent, dataList);
                    }
                };
                adapterTags.setDropDownViewResource(R.layout.empty_layout);
                dropdownDetails.setEnabled(true);
                dropdownDetails.setAdapter(adapterTags);
                adapterTags.notifyDataSetChanged();
            } else {
                if (dataList.get(0).toString().equalsIgnoreCase(Constants.NONE)) {
                    detailText.setText("Details (Optional)");
                }
                dropdownDetails.setEnabled(false);
                tagDropdownArw.setVisibility(View.GONE);
                setFinalAct(0);
                createAds();
            }
        }
    }

    private void updateDrawerSubtype(final ArrayList<String> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.size() > 0) {
                subtypeDropdownArw.setVisibility(View.VISIBLE);
                adapterSubtypes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        actSubtypeDropdownText.setText(((TextView) v).getText());
                        ((TextView) v).setVisibility(View.GONE);
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        return getCustomView(position, convertView, parent, dataList);
                    }
                };
                adapterSubtypes.setDropDownViewResource(R.layout.empty_layout);
                dropdownSubtype.setEnabled(true);
                dropdownSubtype.setAdapter(adapterSubtypes);
                adapterSubtypes.notifyDataSetChanged();
            } else {
                actSubtypeDropdownText.setText(dataList.get(0).toString());
                dropdownSubtype.setEnabled(true);
                subtypeDropdownArw.setVisibility(View.GONE);
            }
        }
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, ArrayList<String> adapterCheckpoint) {
        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout.fragment_checkpoint, parent, false);
        CardView card = (CardView) row.findViewById(R.id.activity_checkpoint_card);
        //// TODO: 8/22/16 fix later with better implementation
        String sub = actSubtypeDropdownText.getText().toString();
        String[] parts = sub.split("\\-");
        //String subT = parts[1];
        String subD = null;
        if (parts.length > 2) {
            subD = parts[2];
            subD = subD.trim();
        }
        String charT = null;
        String subC = adapterCheckpoint.get(position);
        String[] parts1 = subC.split("\\-");
        charT = parts1[0];
        charT = charT.trim();
        String charD = null;
        if (parts1.length > 1) {
            charD = parts1[1];
            charD = charD.trim();
        }
        if (subD == null || charD == null) {
            card.setVisibility(View.VISIBLE);
            RelativeLayout cardLayout = (RelativeLayout) row.findViewById(R.id.activity_checkpoint_card_frag);
            cardLayout.setBackgroundColor(getResources().getColor(R.color.choose_platform_color));
            card.setCardBackgroundColor(getResources().getColor(R.color.traveller_login_email_input_bg));
            TextView label = (TextView) row.findViewById(R.id.activity_checkpoint_text);
            if (adapterCheckpoint != null) {
                label.setText(adapterCheckpoint.get(position));
            }
        } else {
            card.setVisibility(View.VISIBLE);
            RelativeLayout cardLayout = (RelativeLayout) row.findViewById(R.id.activity_checkpoint_card_frag);
            cardLayout.setBackgroundColor(getResources().getColor(R.color.choose_platform_color));
            card.setCardBackgroundColor(getResources().getColor(R.color.traveller_login_email_input_bg));
            TextView label = (TextView) row.findViewById(R.id.activity_checkpoint_text);
            if (adapterCheckpoint != null) {
                label.setText(adapterCheckpoint.get(position));
            }
        }
        return row;
    }

    private View getCheckpointCustomView(int position, View convertView, ViewGroup parent, ArrayList<String> adapterCheckpoint) {
        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout.fragment_checkpoint, parent, false);
        CardView card = (CardView) row.findViewById(R.id.activity_checkpoint_card);
        //// TODO: 8/22/16 fix later with better implementation
        String check = checkpointText.getText().toString();

        if (check.equalsIgnoreCase(adapterCheckpoint.get(position).toString())) {
            //row.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0));
            setLayoutParams(row);
            card.setVisibility(View.GONE);
        } else {
            card.setVisibility(View.VISIBLE);
            RelativeLayout cardLayout = (RelativeLayout) row.findViewById(R.id.activity_checkpoint_card_frag);
            cardLayout.setBackgroundColor(getResources().getColor(R.color.choose_platform_color));
            card.setCardBackgroundColor(getResources().getColor(R.color.traveller_login_email_input_bg));
            TextView label = (TextView) row.findViewById(R.id.activity_checkpoint_text);
            if (adapterCheckpoint != null) {
                label.setText(adapterCheckpoint.get(position));
            }
        }
        return row;
    }

    private View getTagCustomView(int position, View convertView, ViewGroup parent, ArrayList<String> adapterCheckpoint) {
        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout.fragment_checkpoint, parent, false);
        CardView card = (CardView) row.findViewById(R.id.activity_checkpoint_card);
        //// TODO: 8/22/16 fix later with better implementation
        String tag = detailText.getText().toString();

        if (tag.equalsIgnoreCase(adapterCheckpoint.get(position).toString())) {
            //row.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0));
            setLayoutParams(row);
            card.setVisibility(View.GONE);
        } else {
            card.setVisibility(View.VISIBLE);
            RelativeLayout cardLayout = (RelativeLayout) row.findViewById(R.id.activity_checkpoint_card_frag);
            cardLayout.setBackgroundColor(getResources().getColor(R.color.choose_platform_color));
            card.setCardBackgroundColor(getResources().getColor(R.color.traveller_login_email_input_bg));
            TextView label = (TextView) row.findViewById(R.id.activity_checkpoint_text);
            if (adapterCheckpoint != null) {
                label.setText(adapterCheckpoint.get(position));
            }
        }
        return row;
    }

    private void setLayoutParams(View view) {
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            lp.height = 0;
            view.requestLayout();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case TIME_DIALOG_ID:
                Calendar c = Calendar.getInstance();
                // set time picker as current time
                tpd = new TimePickerDialog(this, timePickerListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

                tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
                return tpd;

            case DATE_DIALOG_ID:
                Date d = Util.getCurrentCalendar();
                mDatePickerDai = new DatePickerDialog(this, myDateListener, d.getYear(), d.getMonth(), d.getDate());
                //disable previous dates for datepicker
                mDatePickerDai.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return mDatePickerDai;
            //return new DatePickerDialog(this, myDateListener, d.getYear(), d.getMonth(), d.getDate());

            default:
                return null;
        }
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
//            date_display.setText(arg1 + "-" + arg2+1
//                    + "-" + arg3);
            //showDate(arg1, arg2+1, arg3);

            int m = arg2;
            int d = arg3;
            int y = arg1;

            //todo will change with new calendar but for now hacking to reset date if user selects previous date
            String date = null;
            Date currentDate = Util.getCurrentCalendar();

            Date selectedDate = new Date(y, m, d);

            if (selectedDate.compareTo(currentDate) < 0) {
                Toast.makeText(getApplicationContext(), "Cannot set previous date.", Toast.LENGTH_SHORT).show();
                m = currentDate.getMonth();
                d = currentDate.getDate();
                y = currentDate.getYear();

                //set datepicker view to current date
                updateDatePickerCalendar();
            }
//            else {
            date = m + "-" + d
                    + "-" + y;
//            }

            if (m == 1 && d > 29) {
                m = 3;
                if (d == 30) {
                    d = 1;
                } else {
                    d = 2;
                }
            } else {
                m = m + 1;
            }
            date_display.setText(m + "-" + d
                    + "-" + y);
            Util.checkTimePicker(date_display, time_display, AddFinalActivity.this);
        }
    };

    private String getCreateEventDateTime() {
        if (date_display != null) {
            if ((!date_display.getText().toString().equalsIgnoreCase(getResources().getString(R.string.date_default))) || (!time_display.getText().toString().equalsIgnoreCase(getResources().getString(R.string.time_default)))) {
                try {
                    if (date_display.getText().toString().equalsIgnoreCase(getResources().getString(R.string.date_default))) {
                        date_display.setText(Util.getCurrentDate());
                    }
                    if (time_display.getText().toString().equalsIgnoreCase(getResources().getString(R.string.time_default))) {
                        time_display.setText(Util.getCurrentTime());
                    }
                    String d = Util.changeTimeFormat(time_display.getText().toString(), date_display.getText().toString());
                    return d;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            Calendar c = Calendar.getInstance();
            hour = selectedHour;
            minute = selectedMinute;
            String aTime = null;
            int currHour = c.get(Calendar.HOUR_OF_DAY);
            int currMin = c.get(Calendar.MINUTE);

            if (Util.isPresentDay(AddFinalActivity.this, date_display)) {
                if (hour >= currHour) {
                    if (hour == currHour && minute < currMin) {
                        aTime = Util.getCurrentTime(currHour, currMin, AddFinalActivity.this);
                    } else {
                        // set current time into textview
                        aTime = Util.updateTime(hour, minute);
                    }
                } else {
                    aTime = Util.getCurrentTime(currHour, currMin, AddFinalActivity.this);
                }
            } else {
                // set current time into textview
                aTime = Util.updateTime(hour, minute);
            }
            if (aTime != null) {
                time_display.setText(aTime);
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.event_creation_subtype:
                filterReqList(position);
                break;
            case R.id.event_creation_checkpoint:
                filterReqTagList(position);
                break;
            case R.id.event_creation_detail:
                setFinalAct(position);
                createAds();
                break;
        }
    }

    private void filterReqTagList(int position) {
        String chckpoint = checkpointItems.get(position);
        getTagList(chckpoint);
    }

    private void filterReqList(int position) {
        String sub = actSubTypeList.get(position);
        String[] parts = sub.split("\\-");
        subActType = parts[0];
        subActType = subActType.trim();
        subtypeDifficulty = "";
        if (parts.length > 1) {
            subtypeDifficulty = parts[1];
            subtypeDifficulty = subtypeDifficulty.trim();
        }
        getCheckpointList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateDatePickerCalendar() {
        if (mDatePickerDai != null) {
            if (mDatePickerDai.getDatePicker() != null) {
                Date d = Util.getCurrentCalendar();
                mDatePickerDai.getDatePicker().updateDate(d.getYear(), d.getMonth(), d.getDate());
            }
        }
    }

    private void updateTimePicker() {
        if (tpd != null) {
            Calendar c = Calendar.getInstance();
            tpd.updateTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        }
    }

    private void launchListActivityAndFinish() {
        Intent i = new Intent(this, ListActivityFragment.class);
        startActivity(i);
        finish();
    }

    private void launchListActivityAndFinish(EventData eData) {
        CurrentEventDataHolder ins = CurrentEventDataHolder.getInstance();
        ins.setData(eData);
        Intent i = new Intent(this, ListActivityFragment.class);
        i.putExtra("gotoEventDetail", true);
        startActivity(i);
        finish();
    }

    private void launchEventDetailAndFinish(EventData eData) {
        CurrentEventDataHolder ins = CurrentEventDataHolder.getInstance();
        ins.setData(eData);
        Intent i = new Intent(this, EventDetailActivity.class);
        startActivity(i);
        finish();
    }

    public void showError(String err) {
        hideProgressBar();
        //setErrText(err);
    }

    public void setFinalAct(int position) {
        if (!tagActList.isEmpty()) {
            this.finalAct = tagActList.get(position);
        }
        refreshActivityUI();
    }

    @Override
    public void update(Observable observable, Object data) {
        hideProgressBar();
        if (data != null) {
            if(data instanceof LoginError) {
                setErrText((LoginError) data);
            }else if (data instanceof EventData) {
                launchListActivityAndFinish((EventData) data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (adActivity) {
            launchListActivityAndFinish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ads = false;
        return false;
    }
}
