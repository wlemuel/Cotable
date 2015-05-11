package tk.wlemuel.cotable.utils;

import java.util.Date;
import java.util.TimeZone;

/**
 * Utils for handling the timezone issues.
 *
 * @author HuangWenwei
 * @modified by stevelemuel
 * @created 2014-07-31
 */
public class TimeZoneUtil {

    /**
     * Returns if the timezone of current device is EasternEightZone (China).
     *
     * @return {@code true} if the timezone of current device is EasternEightZone,
     * {@code false} otherwise.
     */
    public static boolean isInEasternEightZones() {
        return TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
    }

    /**
     * Convert the {@code date} according to the timezone.
     *
     * @param date    the current date
     * @param oldZone old timezone
     * @param newZone new timezone
     * @return {@code finalDate} the converted date.
     */
    public static Date transformTime(Date date, TimeZone oldZone,
                                     TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;

    }
}
