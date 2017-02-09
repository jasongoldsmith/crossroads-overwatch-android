package co.crossroadsapp.overwatch;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import co.crossroadsapp.overwatch.data.ConsoleData;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.Util;
import com.loopj.android.http.RequestParams;

import java.util.Observable;
import java.util.Observer;

public class ConsoleSelectionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, Observer {

    private Spinner consoleType;
    private TextView console_name;
    private EditText user_id;
    private TextView next;
    private String console;
    private ControlManager mManager;
    private TextView privacyTerms;
    private WebView webView;
    private ImageView imgConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console_selection);

        mManager = ControlManager.getmInstance();
        mManager.setCurrentActivity(this);

        consoleType = (Spinner) findViewById(R.id.spinner);
        console_name = (TextView) findViewById(R.id.console_name);
        user_id = (EditText) findViewById(R.id.console_id);
        next = (TextView) findViewById(R.id.next_btn);
        next.setEnabled(false);

        imgConsole = (ImageView) findViewById(R.id.console_img);

        privacyTerms = (TextView) findViewById(R.id.privacy_terms);

        webView = (WebView) findViewById(R.id.web);

        setTextViewHTML(privacyTerms, getString(R.string.terms_conditions));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conId = user_id.getText().toString();
                if(conId!=null && console!=null) {
                    showProgressBar();
                    RequestParams rp_console = new RequestParams();
                    rp_console.add("consoleId", conId);
                    rp_console.add("consoleType", console);
                    mManager.verifyBungieId(ConsoleSelectionActivity.this, rp_console);
                }
            }
        });

        user_id.addTextChangedListener(new TextWatcher() {
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

        // Array of console types
        final String[] objects = { "PlayStation 4", "PlayStation 3", "Xbox One", "Xbox 360"};

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, objects){
            int FONT_STYLE = Typeface.BOLD;

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(Typeface.SANS_SERIF, FONT_STYLE);
                ((TextView) v).setTextColor(
                        getResources().getColorStateList(R.color.trimble_white)
                );
                ((TextView) v).setGravity(Gravity.CENTER);

                ((TextView) v).setPadding(Util.dpToPx(0, ConsoleSelectionActivity.this), 0, 0, 0);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                ((TextView) v).setText(((TextView) v).getText());

                if(((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOXONESTRG) || ((TextView) v).getText().toString().equalsIgnoreCase(Constants.CONSOLEXBOX360STRG)) {
                    imgConsole.setImageResource(R.drawable.icon_xboxone_consolex);
                } else {
                    imgConsole.setImageResource(R.drawable.icon_psn_consolex);
                }

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                //View v = super.getDropDownView(position, convertView,parent);

                return getCustomView(position, convertView, parent, objects);

//                        ((TextView) v).setGravity(Gravity.CENTER);
//
//                        v.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
//                        ((TextView) v).setTextColor(getResources().getColor(R.color.trimbe_white));
//                        ((TextView) v).setHeight(Util.dpToPx(50, CreateNewEvent.this));
//
//                        return v;

            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        consoleType.setAdapter(spinnerArrayAdapter);

        //spinnerArrayAdapter.notifyDataSetChanged();

        consoleType.setOnItemSelectedListener(this);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, String[] consoleItems) {

        LayoutInflater inflater=getLayoutInflater();
        View row=inflater.inflate(R.layout.console_selction_view, parent, false);
        ImageView addSymbol = (ImageView)row.findViewById(R.id.console_img);
        if(consoleItems[position].equalsIgnoreCase("PlayStation 4") || consoleItems[position].equalsIgnoreCase("PlayStation 3")){
            addSymbol.setImageDrawable(getResources().getDrawable(R.drawable.icon_psn_consolex));
        } else if(consoleItems[position].equalsIgnoreCase("Xbox One") || consoleItems[position].equalsIgnoreCase("Xbox 360")){
            addSymbol.setImageDrawable(getResources().getDrawable(R.drawable.icon_xboxone_consolex));
        }

        TextView label=(TextView)row.findViewById(R.id.add_console_text);
        if (consoleItems!=null) {
            label.setText(consoleItems[position]);
        }
        return row;
    }

    protected void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void enableSubmitIfReady() {
        if(user_id!=null) {
            if (next!=null) {
                if (user_id.getText().length() > 0) {
                    next.setBackgroundColor(getResources().getColor(R.color.app_theme_color));
                    next.setEnabled(true);
                } else {
                    next.setBackgroundColor(getResources().getColor(R.color.bubble_color));
                    next.setEnabled(false);
                }
            }
        }
    }

    public void showError(String err) {
        hideProgressBar();
        next.setEnabled(true);
        setErrText(err);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        console = parent.getItemAtPosition(position).toString();
        if(console!=null) {
            switch (console)
            {
                case "PlayStation 3" :
                    console_name.setText("PLAYSTATION ID");
                    user_id.setHint("Enter PlayStation ID");
                    console = "PS3";
                    break;
                case "PlayStation 4" :
                    console_name.setText("PLAYSTATION ID");
                    user_id.setHint("Enter PlayStation ID");
                    console = "PS4";
                    break;
                case "Xbox One":
                    console_name.setText("XBOX GAMERTAG");
                    user_id.setHint("Enter Xbox Gamertag");
                    console = "XBOXONE";
                    break;
                case "Xbox 360":
                    console_name.setText("XBOX GAMERTAG");
                    user_id.setHint("Enter Xbox Gamertag");
                    console = "XBOX360";
                    break;

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void update(Observable observable, Object data) {
        hideProgressBar();
        ConsoleData cdata = (ConsoleData) data;
        Intent regIntent = new Intent(getApplicationContext(),
                RegisterActivity.class);
        if (cdata != null) {
            String id = null;
            String memId = null;
            String cType = null;
            if (cdata.getcId() != null) {
                if (cdata.getMembershipId() != null) {
                    if (cdata.getcType() != null) {
                        id = cdata.getcId();
                        Util.setDefaults("consoleId", id, getApplicationContext());

                        memId = cdata.getMembershipId();
                        Util.setDefaults("membershipId", memId, getApplicationContext());

                        cType = cdata.getcType();
                        Util.setDefaults("consoleType", cType, getApplicationContext());

                        startActivity(regIntent);
                        finish();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(webView!=null && webView.getVisibility()== View.VISIBLE) {
            webView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            gotoMainActivity();
        }
    }

    private void gotoMainActivity() {
        Intent signinIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        startActivity(signinIntent);
        finish();
    }
}