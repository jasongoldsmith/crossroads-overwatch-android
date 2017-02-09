package co.crossroadsapp.overwatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class UpdateConsoleActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, Observer {

    private ControlManager mManager;
    private UserData user;
    private ArrayAdapter<String> adapterConsole;
    private ArrayList<String> consoleItems;
    private String selectedConsole=null;
    ArrayList<String> existingConsoles;

    TextView consoleRelatedText;
    RelativeLayout idLayout;
    TextView consoleName;
    EditText consoleEditHint;
    private String console;
    String conId;
    TextView addBtnText;

    AlertDialog dialog;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_console);

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        Bundle b=null;
        if(getIntent()!=null && getIntent().getExtras()!=null) {
            b = getIntent().getExtras();
        }
        //user = b.getParcelable("userdata");
        if(mManager.getUserData()!=null) {
            user = mManager.getUserData();
        } else if(b!=null){
            //user = b.getParcelable("userdata");
        }

        //spinner customization
        Spinner dropdown = (Spinner) findViewById(R.id.spinner_select_console);

        final ImageView imgConsole = (ImageView) findViewById(R.id.select_console_img);

        idLayout  = (RelativeLayout) findViewById(R.id.gamertag_layout);
        consoleRelatedText = (TextView) findViewById(R.id.console_bottom_text);
        consoleName = (TextView) findViewById(R.id.console_name);
        consoleEditHint = (EditText) findViewById(R.id.console_id);

        CardView addBtn = (CardView) findViewById(R.id.add_console);
        addBtnText = (TextView) findViewById(R.id.add_btn_text);

        dropdown.setOnItemSelectedListener(this);
        // Set adapter for console selector
//        ArrayList<String> consoleItems = new ArrayList<String>();
        ArrayList<String> userConsoleList = mManager.getConsoleList();
        existingConsoles = Util.getCorrectConsoleName(userConsoleList);
        consoleItems = Util.getRemConsoleName(userConsoleList);
        if(!consoleItems.isEmpty()) {
            selectedConsole = consoleItems.get(0);
        }

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpgradeDialog();
            }
        });

        setTextForConsole();
        //consoleItems.add(n);
        adapterConsole = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, consoleItems) {

            int FONT_STYLE = Typeface.BOLD;

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(Typeface.SANS_SERIF, FONT_STYLE);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.trimble_white)
                );
                ((TextView) v).setGravity(Gravity.CENTER);

                ((TextView) v).setPadding(Util.dpToPx(0, UpdateConsoleActivity.this), 0, 0, 0);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ((TextView) v).setText(((TextView) v).getText());
                if(((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG) || ((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOX360STRG)) {
                    imgConsole.setImageResource(R.drawable.icon_xboxone_consolex);
                    consoleName.setText("XBOX GAMERTAG");
                    consoleEditHint.setHint("Enter Xbox Gamertag");
                } else {
                    imgConsole.setImageResource(R.drawable.icon_psn_consolex);
                    consoleName.setText("PLAYSTATION ID");
                    consoleEditHint.setHint("Enter PlayStation ID");
                }
                selectedConsole = ((TextView) v).getText().toString();
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getCustomView(position, convertView, parent, consoleItems);
            }
        };
        adapterConsole.setDropDownViewResource(R.layout.empty_layout);
        dropdown.setAdapter(adapterConsole);
        adapterConsole.notifyDataSetChanged();
    }

    private void addConsoleNetworkCall() {
        if(console!=null) {
            if (idLayout.getVisibility() == View.VISIBLE) {
                conId = consoleEditHint.getText().toString();
            } else {
                conId = getConsoleId(console);
            }
            if (conId != null) {
                showProgressBar();
                RequestParams rp_console = new RequestParams();
                rp_console.add("consoleId", conId);
                rp_console.add("consoleType", console);
                mManager.addOtherConsole(UpdateConsoleActivity.this, rp_console);
            }
        }
    }

    private String getConsoleId(String console) {
        if (console.equalsIgnoreCase(Constants.CONSOLEPS4)) {
                for (int n=0;n<user.getConsoles().size();n++) {
                    if(user.getConsoles().get(n).getcType().equalsIgnoreCase(Constants.CONSOLEPS3)) {
                        return user.getConsoles().get(n).getcId();
                    }
                }
        } else if(console.equalsIgnoreCase(Constants.CONSOLEXBOXONE)){
                for (int n=0;n<user.getConsoles().size();n++) {
                    if(user.getConsoles().get(n).getcType().equalsIgnoreCase(Constants.CONSOLEXBOX360)) {
                        return user.getConsoles().get(n).getcId();
                    }
                }
        }
        return null;
    }

    public void showError(String err) {
        hideProgressBar();
        setErrText(err);
    }

    private void setTextForConsole() {
        if (selectedConsole != null) {
            if (!existingConsoles.contains(selectedConsole)) {
                if (selectedConsole.equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG) && existingConsoles.contains(Constants.CONSOLEXBOX360STRG)) {
                    idLayout.setVisibility(View.GONE);
                    consoleRelatedText.setVisibility(View.VISIBLE);
                    consoleRelatedText.setText("NOTE: Once you upgrade to Xbox One, you will no longer be able to view activities from Xbox 360.");
                    if(addBtnText!=null) {
                        addBtnText.setText("UPGRADE");
                    }
                } else if (selectedConsole.equalsIgnoreCase(Constants.CONSOLEPS4STRG) && existingConsoles.contains(Constants.CONSOLEPS3STRG)) {
                    idLayout.setVisibility(View.GONE);
                    consoleRelatedText.setVisibility(View.VISIBLE);
                    consoleRelatedText.setText("NOTE: Once you upgrade to PlayStation 4, you will no longer be able to view activities from PlayStation 3.");
                    if(addBtnText!=null) {
                        addBtnText.setText("UPGRADE");
                    }
                } else {
                    idLayout.setVisibility(View.VISIBLE);
                    consoleRelatedText.setVisibility(View.GONE);
                    if(addBtnText!=null) {
                        addBtnText.setText("ADD");
                    }
                }
            } else {
                idLayout.setVisibility(View.VISIBLE);
                consoleRelatedText.setVisibility(View.GONE);
                if(addBtnText!=null) {
                    addBtnText.setText("ADD");
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        console = parent.getItemAtPosition(position).toString();
        if(console!=null) {
            selectedConsole = console;
            switch (console)
            {
                case "PlayStation 3" :
                    console = "PS3";
                    break;
                case "PlayStation 4" :
                    console = "PS4";
                    break;
                case "Xbox One":
                    console = "XBOXONE";
                    break;
                case "Xbox 360":
                    console = "XBOX360";
                    break;

            }
        }
        setTextForConsole();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //console selector spinner
    private View getCustomView(int position, View convertView, ViewGroup parent, ArrayList<String> consoleItems) {
        LayoutInflater inflater=getLayoutInflater();
        View row=inflater.inflate(R.layout.console_selction_view, parent, false);
        ImageView addSymbol = (ImageView)row.findViewById(R.id.console_img);
        if(consoleItems.get(position).equalsIgnoreCase("PlayStation 4") || consoleItems.get(position).equalsIgnoreCase("PlayStation 3")){
            addSymbol.setImageDrawable(getResources().getDrawable(R.drawable.icon_psn_consolex));
        } else if(consoleItems.get(position).equalsIgnoreCase("Xbox One") || consoleItems.get(position).equalsIgnoreCase("Xbox 360")){
            addSymbol.setImageDrawable(getResources().getDrawable(R.drawable.icon_xboxone_consolex));
        }

        TextView label=(TextView)row.findViewById(R.id.add_console_text);
        if (consoleItems!=null) {
            label.setText(consoleItems.get(position));
        }
        return row;
    }

    private void showUpgradeDialog() {
        if (selectedConsole != null) {
            String s=null;
            if (!existingConsoles.contains(selectedConsole)) {
                if (selectedConsole.equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG) && existingConsoles.contains(Constants.CONSOLEXBOX360STRG)) {
                    s = "Are you sure you want to upgrade to Xbox One? This change will be permanent and you will no longer be able to view activities on Xbox 360.";
                } else if (selectedConsole.equalsIgnoreCase(Constants.CONSOLEPS4STRG) && existingConsoles.contains(Constants.CONSOLEPS3STRG)) {
                    s = "Are you sure you want to upgrade to PlayStation 4? This change will be permanent and you will no longer be able to view activities on PlayStation 3.";
                } else {
                    addConsoleNetworkCall();
                }
                if (s!=null) {
                    createAlert("UPGRADE", s, null, null);
                }
            }
        }
    }

    private void createAlert(String title, String msg, String ok, String cancel) {
        if(title!=null && msg!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(UpdateConsoleActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(UpdateConsoleActivity.this);
            }

            builder.setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("UPGRADE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with finish activity
                            dialog.dismiss();
                            addConsoleNetworkCall();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with finish activity
                            dialog.dismiss();
                        }
                    });
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        hideProgressBar();
        if(data!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(UpdateConsoleActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(UpdateConsoleActivity.this);
            }

            builder.setTitle("Success!")
                    .setMessage("Your " +console + " " + conId + " account has been linked to Crossroads.")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with finish activity
                            dialog.dismiss();
                            finish();
                        }
                    });
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }
}
