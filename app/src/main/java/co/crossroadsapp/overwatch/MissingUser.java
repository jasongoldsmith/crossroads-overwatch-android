package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MissingUser extends Activity {

    private String userId;
    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_user);

        Bundle b = getIntent().getExtras();
        if(b!=null) {
//            if (b.containsKey("id")) {
//                userId = b.getString("id");
//            }
            if (b.containsKey("error")) {
                error = b.getString("error");
            }
        }

        ImageView backBtn = (ImageView) findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView contactUsBtn = (TextView) findViewById(R.id.contact_us_btn);
        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        CrashReport.class);
                startActivity(intent);
                finish();
            }
        });

        //TextView id = (TextView) findViewById(R.id.id_text);
        TextView id_text = (TextView) findViewById(R.id.misadv_text);
        //TextView contactUsMsg = (TextView) findViewById(R.id.user_missing_email);

        ///contactUsMsg.setText(Html.fromHtml(getString(R.string.user_missing_text)));
        //contactUsMsg.setMovementMethod(LinkMovementMethod.getInstance());

//        if(userId!=null) {
//            id.setText(userId);
//        }
        if(error!=null && !error.isEmpty()) {
            id_text.setText(error);
        }
    }
}
