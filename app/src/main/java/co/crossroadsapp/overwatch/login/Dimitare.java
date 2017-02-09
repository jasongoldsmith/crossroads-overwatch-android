package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import co.crossroadsapp.overwatch.R;

/**
 * Created by karagdi on 2/1/17.
 */
//https://android-developers.googleblog.com/2009/04/updating-applications-for-on-screen.html
public class Dimitare extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dimitare_adjustpan);
        /*View open = findViewById(R.id.edtInput);
        if( open != null )
        {
            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }*/
    }
}
