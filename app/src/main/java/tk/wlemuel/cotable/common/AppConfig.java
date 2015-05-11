package tk.wlemuel.cotable.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * AppConfig
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc AppConfig
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class AppConfig {

    public final static String TAG = AppConfig.class.getSimpleName();

    public final static String APP_CONFIG = "app_config";
    public final static String APP_NAME = "Cotadle";
    public final static String UTF8 = "utf-8";
    public final static int DEFAULT_PAGE_SIZE = 20;

    // Set the image and cache directory.
    public final static String DOWNLOAD_IMAGE_DIR = "image";
    public final static String SAVE_IMAGE_PATH = "SAVE_IMAGE_PATH";
    public final static String DEFAULT_IMAGE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + APP_NAME + File.separator + DOWNLOAD_IMAGE_DIR + File.separator;
    public final static String DFAULT_CACHE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + APP_NAME + File.separator + "cache" + File.separator;

    public final static String CONF_LOAD_IMAGE = "CONF_LOAD_IMAGE";
    public final static String CONF_PAGE_SIZE = "CONF_PAGE_SIZE";

    // Other configurations
    public final static int SEARCH_ICON_SIZE_PATCH = 20;


    private Context _context;
    private static AppConfig _appConfig;

    public static AppConfig getAppConfig() {
        if (_appConfig == null) _appConfig = new AppConfig();

        return _appConfig;
    }

    public static AppConfig getAppConfig(Context context) {
        getAppConfig()._context = context;
        return _appConfig;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isLoadImage(Context context) {
        return getSharedPreferences(context).getBoolean(CONF_LOAD_IMAGE, true);
    }

    public static int getPageSize(Context context){
        return getSharedPreferences(context).getInt(CONF_PAGE_SIZE, DEFAULT_PAGE_SIZE);
    }


    public String get(String key) {
        Properties props = get();
        return (props == null) ? null : props.getProperty(key);
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();

        File dirConf = _context.getDir(APP_CONFIG, Context.MODE_MULTI_PROCESS);
        try {
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return props;
    }

    private void setProps(Properties props){
        FileOutputStream fos = null;
        try{
            File dirConf = _context.getDir(APP_CONFIG, Context.MODE_MULTI_PROCESS);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            props.store(fos, null);
            fos.flush();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void set(Properties p){
        Properties props = get();
        p.putAll(p);
        setProps(props);
    }

    public void set(String key, String value){
        Properties props = get();
        props.put(key, value);
        setProps(props);
    }

    public void remove(String... keys){
        Properties props = get();
        for(String key : keys)
            props.remove(key);

        setProps(props);
    }

}
