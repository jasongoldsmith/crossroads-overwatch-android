package co.crossroadsapp.overwatch.token;

/**
 * Created by sharmha on 10/12/16.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import co.crossroadsapp.overwatch.R;

public class TokenTextView extends TextView {

    public TokenTextView(Context context) {
        super(context);
    }

    public TokenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.drawable.icon_close_x : 0, 0);
    }
}
