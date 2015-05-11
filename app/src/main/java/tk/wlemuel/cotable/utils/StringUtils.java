package tk.wlemuel.cotable.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Utils for handling the String.
 *
 * @author liux
 * @version 1.0
 * @modified by stevelemuel
 * @created 2012-3-21
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    // private final static SimpleDateFormat dateFormater = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // private final static SimpleDateFormat dateFormater2 = new
    // SimpleDateFormat("yyyy-MM-dd");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new
            ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
            };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new
            ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }
            };

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    /**
     * Convert {@code sdate} to {@code Date} type.
     *
     * @param sdate the date stored with String type.
     * @return {@code Date} the converted date.
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * Conver {@code date} to {@code String} type.
     *
     * @param date the given date.
     * @return {@code String} the converted String date
     */
    public static String dateToString(Date date) {
        if (date == null) return null;

        return dateFormater.get().format(date);
    }

    /**
     * Convert {@code sdata} to {@code Date} type. (for Cnblogs only)
     *
     * @param sdate the date stored with String type.
     * @return {@code Date} the converted date.
     */
    public static Date toDateFromCnblogs(String sdate) {
        if (sdate == null) return null;

        sdate = sdate.replace("T", " ");
        sdate = sdate.replace("Z", "");

        return toDate(sdate);
    }

    /**
     * Show the time friendly, such as "10 minutes ago".
     *
     * @param sdate the date stored with String type.
     * @return
     */
    public static String friendly_time(String sdate) {

        if (sdate == null || sdate == "") return "";
//        TODO make the text restrive from the strings.xml
        final String _hours_ago = "小时前";
        final String _minutes_ago = "分钟前";
        final String _days_ago = "天前";
        final String _an_hour_ago = "1小时前";
        final String _a_minute_ago = "1分钟前";
        final String _yesterday = "昨天";
        final String _the_day_before_yesterday = "前天";

        Date time = null;
        if (TimeZoneUtil.isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = TimeZoneUtil.transformTime(toDate(sdate),
                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // Judge whether it is today or not.
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            int minute = (int) ((cal.getTimeInMillis() - time.getTime()) / 60000);
            if (hour == 0) {
                if (minute == 1)
                    return _a_minute_ago;

                ftime = Math.max(
                        minute, 1)
                        + _minutes_ago;
            } else if (hour == 1)
                return _an_hour_ago;
            else
                ftime = hour + _hours_ago;
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            int minute = (int) ((cal.getTimeInMillis() - time.getTime()) / 60000);
            if (hour == 0) {
                if (minute == 1)
                    return _a_minute_ago;

                ftime = Math.max(
                        minute, 1)
                        + _minutes_ago;
            } else if (hour == 1)
                return _an_hour_ago;
            else
                ftime = hour + _hours_ago;
        } else if (days == 1) {
            ftime = _yesterday;
        } else if (days == 2) {
            ftime = _the_day_before_yesterday;
        } else if (days > 2 && days <= 10) {
            ftime = days + _days_ago;
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * Returns if the given {@code sdate} is today.
     *
     * @param sdate the date stored with String type.
     * @return {@code true} if the given date is today,
     * {@code false} otherwise.
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * Returns the date of today with long type.
     *
     * @return {@code long} today
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    /**
     * Returns if the given String is empty
     *
     * @param input the input data
     * @return {@code true} if {@code input} is one of null, "", "\t", "\r" and "\n",
     * {@code false} otherwise.
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns if {@code email} is a valid email address.
     *
     * @param email the email address
     * @return {@code true} if {@code email} is a valid email address,
     * {@code false} otherwise.
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * Convert String to Int
     *
     * @param str      the input date
     * @param defValue the default value if the given {@code str} is unavailable or any exception occurrs.
     * @return {@code int}
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * Convert Object to Int
     *
     * @param obj an object
     * @return {@code int} if convert successfully,
     * 0 otherwise.
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * Convert String to Long.
     *
     * @param str the string
     * @return {@code long} if convert successfully,
     * 0 otherwise.
     */
    public static long toLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Convert String to Boolean.
     *
     * @param str the string
     * @return {@code true} if convert successfully,
     * {@code false} otherwise.
     */
    public static boolean toBool(String str) {
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Convert the InputStream to String.
     *
     * @param is the InputStream
     * @return {@code String} if convert successfully,
     * "" otherwise.
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr = null;
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    /**
     * Convert the given String to URL.
     *
     * @param url the link address
     * @return {@code URL} if convert successfully, null otherwise.
     */
    public static URL toUrl(String url) {
        URL u = null;

        if (url == null || url.isEmpty()) url = "";

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return u;
    }

}
