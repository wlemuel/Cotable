package tk.wlemuel.cotable.model;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.activity.blog.fragment.BlogDetailFragment;
import tk.wlemuel.cotable.activity.settings.fragment.AboutFragment;
import tk.wlemuel.cotable.activity.settings.fragment.SettingFragment;

/**
 * SimpleBackPage
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc SimpleBackPage
 * @created 2015/05/12
 * @updated 2015/05/12
 */
public enum SimpleBackPage {

    SETTING(0, R.string.actionbar_title_setting, SettingFragment.class),
    ABOUT(1, R.string.actionbar_title_about, AboutFragment.class),
    DETAILS(2, R.string.actionbar_title_blogdetail, BlogDetailFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    SimpleBackPage(int value, int title, Class<?> clz) {
        this.title = title;
        this.clz = clz;
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int value) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == value) {
                return p;
            }
        }

        return null;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
