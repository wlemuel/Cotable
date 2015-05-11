package tk.wlemuel.cotable.activity.blog;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.activity.blog.fragment.BlogFragment;
import tk.wlemuel.cotable.model.BlogList;

/**
 * BlogTab
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogTab
 * @created 2015/05/10
 * @updated 2015/05/10
 */
public enum BlogTab {
    LASTEST(0, BlogList.CATALOG_LASTEST, R.string.tab_title_blog_latest, BlogFragment.class),
    RECOMMEND(1, BlogList.CATALOG_RECOMMEND, R.string.tab_title_blog_recommend,
            BlogFragment.class);

    private Class<?> clz;
    private int idx;
    private int title;
    private int catalog;

    BlogTab(int idx, int catalog, int title, Class<?> clz) {
        this.idx = idx;
        this.clz = clz;
        this.setCatalog(catalog);
        this.setTitle(title);
    }

    public static BlogTab getTabByIdx(int idx) {
        for (BlogTab t : values()) {
            if (t.getIdx() == idx)
                return t;
        }
        return LASTEST;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }
}
