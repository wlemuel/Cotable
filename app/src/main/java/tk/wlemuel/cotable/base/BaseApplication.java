package tk.wlemuel.cotable.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.utils.TDevice;
import tk.wlemuel.cotable.utils.TLog;

/**
 * BaseApplication
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseApplication
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class BaseApplication extends Application {

    public final static String TAG = BaseApplication.class.getSimpleName();

    public final static String PREF_NAME = "CreativeLocker.pref";
    public final static String DEFAULT_STRING = "";

    static Context _context;
    static Resources _resources;

    private static String lastToast = "";
    private static long lastToastTime;

    private static boolean isAtLeastGB;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            isAtLeastGB = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the context and resources.
        _context = getApplicationContext();
        _resources = _context.getResources();


        init();
    }

    /**
     * Other init actions.
     */
    protected void init() {

    }

    /**
     * Fetch the context of this application.
     *
     * @return {@code _context}
     */
    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }


    /**
     * Fetch the resource of this application.
     *
     * @return {@code _resources}
     */
    public static Resources resources() {
        return _resources;
    }


    /**
     * Get the persist perferences.
     *
     * @return the SharedPreferences if successful.
     */
    public static SharedPreferences getPresistPerferences() {
        return context().getSharedPreferences(PREF_NAME, isAtLeastGB ? Context.MODE_MULTI_PROCESS
                : Context.MODE_PRIVATE);
    }

    /**
     * Choose the difference editor apply behavior according to the device version.
     *
     * @param editor the sharedPerfernce.Editor
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(Editor editor) {
        if (isAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * Get the perferences.
     *
     * @return the SharedPreferences if successful.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        return context().getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
    }

    /**
     * Returns the display size with the 2-dimension array.
     *
     * @return int[]
     */
    public static int[] getDisplaySize() {
        return new int[]{getPreferences().getInt("screen_width", 480),
                getPreferences().getInt("screen_height", 800)};
    }

    /**
     * Save the display size of Activity.
     *
     * @param activity an activity
     */
    public static void saveDisplaySize(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        Editor editor = getPreferences().edit();
        editor.putInt("screen_width", displaymetrics.widthPixels);
        editor.putInt("screen_height", displaymetrics.heightPixels);
        editor.putFloat("density", displaymetrics.density);
        apply(editor);
        TLog.log("", "Resolution:" + displaymetrics.widthPixels + " x "
                + displaymetrics.heightPixels + " DisplayMetrics:" + displaymetrics.density
                + " " + displaymetrics.densityDpi);
    }

    /**
     * Fetch the string according to the given {@code resId}.
     *
     * @param resId the resource id
     * @return the string if successfully, {@code DEFAULT_STRING} otherwise.
     */
    public static String string(int resId) {
        if (resources() == null) return DEFAULT_STRING;
        return resources().getString(resId);

    }

    /**
     * Fetch the string according to the given {@code resId} and {@code args}.
     *
     * @param resId the resource id
     * @return the string if successfully, {@code DEFAULT_STRING} otherwise.
     */
    public static String string(int resId, Object... args) {
        if (resources() == null) return DEFAULT_STRING;
        return resources().getString(resId, args);
    }


    /**
     * Show the toast for the current application.
     *
     * @param message  the message content
     * @param duration the duration
     * @param icon     the icon resource
     * @param gravity  the gravity of the toast
     */
    public static void showToast(String message, int duration, int icon, int gravity) {
        if (message == null || message.equalsIgnoreCase("")) return;
        long time = System.currentTimeMillis();
        if (!message.equalsIgnoreCase(lastToast) || Math.abs(time - lastToastTime) > 2000) {
            View view = LayoutInflater.from(context()).inflate(
                    R.layout.view_toast, null);
            ((TextView) view.findViewById(R.id.tv_text)).setText(message);
            if (icon != 0) {
                ImageView mIcon = (ImageView) view.findViewById(R.id.iv_icon);
                mIcon.setImageResource(icon);
                mIcon.setVisibility(View.VISIBLE);
            }

            Toast toast = new Toast(context());
            toast.setView(view);
            toast.setGravity(gravity, 0, TDevice.getActionBarHeight(context()));
            toast.setDuration(duration);
            toast.show();

            lastToast = message;
            lastToastTime = System.currentTimeMillis();

        }


    }

    /**
     * Show the toast.
     *
     * @param message the message content.
     */
    public static void showToast(String message) {
        showToast(message, 0);
    }

    /**
     * Show the toast.
     *
     * @param message the message content.
     * @param icon    the icon resource
     */
    public static void showToast(String message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon, Gravity.FILL_HORIZONTAL | Gravity.TOP);
    }

    /**
     * Show the toast.
     *
     * @param message the message content.
     * @param icon    the icon resource
     */
    public static void showToastShort(String message, int icon) {
        showToast(message, Toast.LENGTH_SHORT, icon, Gravity.FILL_HORIZONTAL | Gravity.TOP);
    }

    /**
     * Show the toast.
     *
     * @param message the message content.
     */
    public static void showToastShort(String message) {
        showToastShort(message, 0);
    }

}
