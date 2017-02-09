package co.crossroadsapp.overwatch.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sharmha on 3/3/16.
 */
public class TravellerLog {

    //private static LogToFile logToFile;
    private static boolean enableLog = false;
    private static boolean DEBUG = false;
    //private static boolean DEBUG = ( AppEngine.DEBUG_APP == 0 ? true : false );

    static
    {
        if( enableLog )
        {
//            try
//            {
//                //logToFile = new LogToFile();
//            }
//            catch( RuntimeException e )
//            {
//                Log.e( "Trimble-TrimbleLog", e.toString() );
//            }
//            catch( IOException e )
//            {
//                Log.e( "Trimble-TrimbleLog", e.toString() );
//            }
        }
    }

    private static final String LOG_TAG = "Traveler-";

    private static String getClassName(Object obj )
    {
        String strName = "";
        if( obj instanceof String)
        {
            strName = (String)obj;
        }
        else
        {
            strName = obj.getClass().getName();
            int index = strName.lastIndexOf( '.' );
            if( index != -1 )
            {
                strName = strName.substring( index + 1 );
            }
        }
        return LOG_TAG + strName;
    }

    public static void setLogEnable( boolean enabled )
    {
        enableLog = enabled;
        DEBUG = enabled;
    }

    public static void v(Object obj, String msg )
    {
        if( !DebugOn() )
        {
            return;
        }
        String strClassName = getClassName( obj );

        Log.println( Log.VERBOSE, strClassName, msg );

//        if( logToFile != null && enableLog )
//        {
//            logToFile.writeLogToFile( Log.VERBOSE, strClassName, msg );
//        }
    }

    public static void throwToast(Activity act, String error )
    {
        if( !DebugOn() )
        {
            return;
        }
        Toast.makeText(act, error, Toast.LENGTH_LONG);
    }

    public static void d(Object obj, String msg )
    {
        if( !DebugOn() )
        {
            return;
        }
        String strClassName = getClassName( obj );

        Log.println( Log.DEBUG, strClassName, msg );

//        if( logToFile != null && enableLog )
//        {
//            logToFile.writeLogToFile( Log.DEBUG, strClassName, msg );
//        }
    }

    public static void i(Object obj, String msg )
    {
        if( !DebugOn() )
        {
            return;
        }
        String strClassName = getClassName( obj );

        Log.println( Log.INFO, strClassName, msg );

//        if( logToFile != null && enableLog )
//        {
//            logToFile.writeLogToFile( Log.INFO, strClassName, msg );
//        }
    }

    public static void w(Object obj, String msg )
    {
        if( !DebugOn() )
        {
            return;
        }
        String strClassName = getClassName( obj );

        Log.println(Log.WARN, strClassName, msg);

//        if( logToFile != null && enableLog )
//        {
//            logToFile.writeLogToFile( Log.WARN, strClassName, msg );
//        }
    }

    public static void e(Object obj, String msg )
    {
        if( !DebugOn() )
        {
            return;
        }
        String strClassName = getClassName( obj );

        Log.println( Log.ERROR, strClassName, msg );

//        if( logToFile != null && enableLog )
//        {
//            logToFile.writeLogToFile( Log.ERROR, strClassName, msg );
//        }
    }

    private static boolean DebugOn()
    {
        return DEBUG;
    }

//    public static void d(String tag, String message) {
//        if (BuildConfig.DEBUG) {
//            Log.d(tag, message);
//        }
//    }
//
//    public static void i(String tag, String message) {
//        if (BuildConfig.DEBUG) {
//            Log.i(tag, message);
//        }
//    }
//
//    public static void v(String tag, String message) {
//        if (BuildConfig.DEBUG) {
//            Log.v(tag, message);
//        }
//    }
//
//    public static void w(String tag, String message) {
//        if (BuildConfig.DEBUG) {
//            Log.w(tag, message);
//        }
//    }
//
//    public static void e(String tag, String message) {
//        Log.e(tag, message);
//    }
}
