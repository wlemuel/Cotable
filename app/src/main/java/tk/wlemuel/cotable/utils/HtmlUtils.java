package tk.wlemuel.cotable.utils;

/**
 * HtmlUtils
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc HtmlUtils
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public class HtmlUtils {

    private static final String LESS_THEN = "&lt;";
    private static final String GREAT_THEN = "&gt;";
    private static final String AND = "&amp;";
    private static final String QUOTE = "&quot;";

    private static final String REAL_LESS_THEN = "<";
    private static final String REAL_GREAT_THEN = ">";
    private static final String REAL_AND = "&";
    private static final String REAL_QUOTE = "\"";

    public static String filter(String org) {
        if (org == null || org == "") return "";
        org = org.replace(LESS_THEN, REAL_LESS_THEN);
        org = org.replace(GREAT_THEN, REAL_GREAT_THEN);
        org = org.replace(AND, REAL_AND);
        org = org.replace(QUOTE, REAL_QUOTE);

        return org;
    }
}
