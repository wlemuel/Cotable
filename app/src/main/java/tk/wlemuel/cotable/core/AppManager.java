package tk.wlemuel.cotable.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * AppManager
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc Manage the activities of current application.
 * @created 2015/05/18
 * @updated 2015/05/18
 */
public class AppManager {

    private static Stack<Activity> mActivityStack;
    private static AppManager mInstance;

    private AppManager() {
    }


    /**
     * Simple Singleton Design pattern.
     *
     * @return {@code AppManager}
     */
    public static AppManager getAppManager() {
        if (mInstance == null) mInstance = new AppManager();

        return mInstance;
    }

    /**
     * Add activity to the stack.
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) mActivityStack = new Stack<>();

        mActivityStack.add(activity);
    }

    /**
     * Returns the current activity at the top of stack.
     *
     * @return Activity if the stack exists, null otherwise.
     */
    public Activity currentActivity() {
        if (mActivityStack == null) return null;
        return mActivityStack.lastElement();
    }

    /**
     * Finish the given activity.
     *
     * @param activity given activity.
     */
    public void finishActivity(Activity activity) {
        if (mActivityStack != null && activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * Finish the current activity.
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     * Finish the activities according to the given class type.
     *
     * @param clz the class type
     */
    public void finishActivity(Class<?> clz) {
        if (mActivityStack == null) return;

        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(clz))
                finishActivity(activity);
        }
    }

    /**
     * Finish all the activities.
     */
    public void finishAllActivities() {
        if (mActivityStack == null) return;

        for (Activity activity : mActivityStack) {
            if (null != activity)
                finishActivity(activity);
        }
    }

    /**
     * Exit the application.
     *
     * @param context given context
     */
    public void AppExit(Context context) {
        try {
            finishAllActivities();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                    .ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
