package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

public class PasswordReset extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CardView bungieBtn = (CardView) findViewById(R.id.bungie_btn);
        bungieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://www.bungie.net";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                PasswordReset.this.startActivity(browserIntent);
            }
        });
    }
}
