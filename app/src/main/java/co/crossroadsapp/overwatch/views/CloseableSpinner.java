package co.crossroadsapp.overwatch.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by karagdi on 2/1/17.
 */
public class CloseableSpinner extends Spinner {
    public CloseableSpinner(Context context) {
        super(context);
    }

    public CloseableSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CloseableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CloseableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CloseableSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
