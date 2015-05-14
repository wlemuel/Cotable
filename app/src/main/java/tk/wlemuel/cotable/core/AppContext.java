package tk.wlemuel.cotable.core;

import android.annotation.TargetApi;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

import tk.wlemuel.cotable.api.ApiHttpClient;
import tk.wlemuel.cotable.base.BaseApplication;
import tk.wlemuel.cotable.utils.StringUtils;

/**
 * AppContext
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc AppContext
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class AppContext extends BaseApplication {

    public static final String TAG = AppContext.class.getSimpleName();

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    private static final int CACHE_TIME = 60 * 6000;

    private static final String KEY_USE_SSL = "KEY_USE_SSL";
    private static final String KEY_SOFTKEYBOARD_HEIGHT = "KEY_SOFTKEYBOARD_HEIGHT";
    private static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    private static final String KEY_BLOG_LASTEST = "KEY_BLOG_LASTEST";
    private static final String KEY_BLOG_RECOMMENT = "KEY_BLOG_RECOMMENT";
    private static final String KEY_NEWS_LASTEST = "KEY_NEWS_LASTEST";
    private static final String KEY_AUTOLOAD_NEXT_CONTENTS = "KEY_AUTOLOAD_NEXT_CONTENTS";
    private static AppContext mInstance;
    private String saveImagePath;

    public static int getSoftKeyboardHeight() {
        return getPreferences().getInt(KEY_SOFTKEYBOARD_HEIGHT, 0);
    }

    public static void setSoftKeyboardHeight(int height) {
        Editor editor = getPreferences().edit();
        editor.putInt(KEY_SOFTKEYBOARD_HEIGHT, height);
        apply(editor);
    }

    public static boolean shouldLoadImage() {
        return getPreferences().getBoolean(KEY_LOAD_IMAGE, true);
    }

    public static void setLoadImage(boolean flag) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_LOAD_IMAGE, flag);
        apply(editor);
    }

    public static boolean isAutoloadNextContents() {
        return getPreferences().getBoolean(KEY_AUTOLOAD_NEXT_CONTENTS, true);
    }

    public static void setAutoloadNextContents(boolean flag) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_AUTOLOAD_NEXT_CONTENTS, flag);
        apply(editor);
    }

    /**
     * Check if the primary "external" storage device is available.
     *
     * @return true if available, false otherwise
     */
    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * Get the usable storage space.
     *
     * @param path the file path
     * @return the size of usable space
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (path == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        } else {
            if (!path.exists()) {
                return 0;
            } else {
                final StatFs stats = new StatFs(path.getPath());
                return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            }
        }
    }

    public static void setRefreshTime(String cacheKey, long time) {
        Editor editor = getPreferences().edit();
        editor.putLong(cacheKey, time);
        apply(editor);
    }

    public static long getRefreshTime(String cacheKey) {
        return getPreferences().getLong(cacheKey, 0);
    }

    public static String getFromAssets(String filename) {
        try {
            InputStreamReader isr = new InputStreamReader(
                    context().getResources().getAssets().open(filename)
            );

            BufferedReader br = new BufferedReader(isr);

            String line;
            String result = "";

            while ((line = br.readLine()) != null)
                result += line;

            br.close();
            isr.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void init() {
        super.init();

        mInstance = this;

        init2();

    }

    private void init2() {
        // Set the image path
        saveImagePath = getProperty(AppConfig.SAVE_IMAGE_PATH);
        if (StringUtils.isEmpty(saveImagePath)) {
            setProperty(AppConfig.SAVE_IMAGE_PATH,
                    AppConfig.DEFAULT_IMAGE_PATH);
            saveImagePath = AppConfig.DEFAULT_IMAGE_PATH;
        }

        // Init the AsyncHttpClient
        ApiHttpClient.getHttpClient();
    }

    /**
     * Check the cache file is expired or not.
     *
     * @param cacheFile Cache File.
     * @return true if cache data expired, false otherwise.
     */
    public boolean isCacheDataFailure(String cacheFile) {
        boolean failure = false;
        File data = getFileStreamPath(cacheFile);
        if (data.exists()
                && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;

        return failure;
    }

    /**
     * Get the current active network status.
     *
     * @return 0: no network
     * 1: WIFI
     * 2: WAP
     * 3: CMNET
     */
    public int getNetWorkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) return netType;

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet"))
                    netType = NETTYPE_CMNET;
                else
                    netType = NETTYPE_CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }

        return netType;

    }

    /**
     * Check the current active network is available.
     *
     * @return true if the network connected, false otherwise.
     */
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Get the package information.
     *
     * @return PackageInfo
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (info == null) info = new PackageInfo();

        return info;
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperties(Properties p) {
        AppConfig.getAppConfig(this).set(p);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void removeProperty(String... keys) {
        AppConfig.getAppConfig(this).remove(keys);
    }
}
