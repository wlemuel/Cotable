package tk.wlemuel.cotable.cache;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * CacheManager
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc CacheManager
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public class CacheManager {

    /**
     * Save the object
     *
     * @param context context
     * @param ser     serializable object
     * @param file    cache file
     * @throws java.io.IOException IOException
     */
    @SuppressWarnings("JavaDoc")
    public static boolean saveObject(Context context, Serializable ser,
                                     String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (oos != null) oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read the object
     *
     * @param context context
     * @param file    cache file
     * @return serializable object
     * @throws java.io.IOException Exception
     */
    @SuppressWarnings("JavaDoc")
    public static Serializable readObject(Context context, String file) {
        if (!isExistDataCache(context, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            // Delete the cache file if the deserialization failed.
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                //noinspection ResultOfMethodCallIgnored
                data.delete();
            }
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Judge whether the cache file is readable.
     *
     * @param context   context
     * @param cachefile cache file
     * @return true if the cache file is readable, false otherwise.
     */
    public static boolean isReadDataCache(Context context, String cachefile) {
        return readObject(context, cachefile) != null;
    }

    /**
     * Juget whether the cache file exists.
     *
     * @param context   context
     * @param cachefile cache file
     * @return true if the cache data exists, false otherwise.
     */
    public static boolean isExistDataCache(Context context, String cachefile) {
        if (context == null)
            return false;
        boolean exist = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }


}
