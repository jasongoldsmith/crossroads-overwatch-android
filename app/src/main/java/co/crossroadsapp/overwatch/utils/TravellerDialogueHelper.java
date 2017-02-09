package co.crossroadsapp.overwatch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import co.crossroadsapp.overwatch.R;

/**
 * Created by sharmha on 3/18/16.
 */
public class TravellerDialogueHelper {

    public static AlertDialog.Builder createBaseDialogBuilder(final Activity activity, String title, String message, DialogInterface.OnClickListener onClickListener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setTitle( title );
        builder.setMessage(message);
        builder.setInverseBackgroundForced( true );
        builder.setPositiveButton( R.string.download_btn, null );
        return builder;
    }

    public static AlertDialog.Builder createConfirmDialogBuilder(final Activity activity, String title, String message, String positiveBtn, String negativeBtn, DialogInterface.OnClickListener onClickListener )
    {
        AlertDialog.Builder builder = createAlertDialogBuilder(activity, title, message, positiveBtn, onClickListener);
        if( negativeBtn != null && negativeBtn.length() > 0 ) {
            builder.setNegativeButton(negativeBtn, onClickListener);
        }
        return builder;
    }

    public static AlertDialog.Builder createAlertDialogBuilder(final Activity mActivity, String title, String message, String positiveBtn, DialogInterface.OnClickListener onClickListener )
    {
        AlertDialog.Builder builder = createBaseDialogBuilder(mActivity, title, positiveBtn, null);
        builder.setMessage( message );
        builder.setCancelable(false);
        return builder;
    }
}
