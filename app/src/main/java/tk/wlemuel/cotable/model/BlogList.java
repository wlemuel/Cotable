package tk.wlemuel.cotable.model;

import java.util.ArrayList;
import java.util.List;

/**
 * BlogList
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogList
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public class BlogList extends Entity implements ListEntity{

    public static final int CATALOG_LASTEST = 1;
    public static final int CATALOG_RECOMMEND = 2;

    public static final String TYPE_LASTEST = "BLOG_LASTEST";
    public static final String TYPE_RECOMMEND = "BLOG_RECOMMEND";

    private List<Blog> bloglist = new ArrayList<>();
    private int pageSize;
    private int blogCount;

    public int getPageSize() {
        return pageSize;
    }

    public int getBlogCount() {
        return blogCount;
    }

    public List<Blog> getBloglist() {
        return bloglist;
    }

    public static BlogList parse(String data){
        BlogList bloglist = new BlogList();
        bloglist.getBloglist().addAll(Blog.parse(data));
        return bloglist;
    }

    @Override
    public List<?> getList() {
        return bloglist;
    }
}
