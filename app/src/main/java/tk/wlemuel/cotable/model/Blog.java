package tk.wlemuel.cotable.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tk.wlemuel.cotable.utils.StringUtils;

/**
 * Blog
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc Blog
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public class Blog extends Base {

    private final static String TAG_ENTRY = "entry";
    private final static String TAG_ID = "id";
    private final static String TAG_TITLE = "title";
    private final static String TAG_SUMMARY = "summary";
    private final static String TAG_PUBLISHED = "published";
    private final static String TAG_UPDATED = "updated";
    private final static String TAG_AUTHOR_NAME = "name";
    private final static String TAG_AUTHOR_URI = "uri";
    private final static String TAG_AUTHOR_AVATAR = "avatar";
    private final static String TAG_LINK = "link";
    private final static String TAG_LINK_HREF = "href";
    private final static String TAG_BLOGAPP = "blogapp";
    private final static String TAG_DIGGS = "diggs";
    private final static String TAG_VIEWS = "views";
    private final static String TAG_COMMENTS = "comments";

    private final static String BLOG_LINK_DILIMITER = "p/";

    private int blogId;
    private String title;
    private String summary;
    private Date published;
    private Date updated;

    private String author_name;
    private URL author_uri;
    private String author_avatar;

    private URL url;
    private String blogapp;
    private int diggs;
    private int reads;
    private int comments;

    private String postId;

    /**
     * Parse the dato to Blog List.
     *
     * @param data the data contains the blog infos.
     * @return the Blog List
     */
    public static List<Blog> parse(String data) {
        List<Blog> blogList = null;
        Blog blog = null;

        if (data != null && !data.equals("")) try {
            JSONTokener jsonParser = new JSONTokener(data);

            JSONObject content = (JSONObject) jsonParser.nextValue();
            JSONArray list = content.getJSONArray("data");

            if (list.length() > 0) blogList = new ArrayList<>();

            for (int i = 0; i < list.length(); ++i) {
                JSONObject info = (JSONObject) list.get(i);
                blog = new Blog();
                blog.setAuthor_name(info.getString("author"));
                blog.setPostId(info.getString("blog_id"));
                blog.setUrl(StringUtils.toUrl(info.getString("blog_url")));
                blog.setBlogId(StringUtils.toInt(info.getString("blog_id")));
                blog.setBlogapp(info.getString("blogapp"));
                blog.setComments(StringUtils.toInt(info.getString("comment")));
                blog.setSummary(info.getString("content"));
                blog.setReads(StringUtils.toInt(info.getString("hit")));
                blog.setTitle(info.getString("title"));
                blog.setUpdated(StringUtils.toDate(info.getString("public_time")));

                if (blogList != null) {
                    blogList.add(blog);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return blogList;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getUpdated() {
        if (updated != null) {
            return StringUtils.friendly_time(
                    StringUtils.dateToString(updated)
            );
        }

        return StringUtils.dateToString(new Date());
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public URL getAuthor_uri() {
        return author_uri;
    }

    public void setAuthor_uri(URL author_uri) {
        this.author_uri = author_uri;
    }

    public String getAuthor_avatar() {
        return "http://pic.cnblogs.com/face/u34358.jpg";
    }

    public void setAuthor_avatar(String author_avatar) {
        this.author_avatar = author_avatar;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getBlogapp() {
        return blogapp;
    }

    public void setBlogapp(String blogapp) {
        this.blogapp = blogapp;
    }

    public int getDiggs() {
        return diggs;
    }

    public void setDiggs(int diggs) {
        this.diggs = diggs;
    }

    public int getReads() {
        return reads;
    }

    public void setReads(int reads) {
        this.reads = reads;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Blog) {
            Blog blog = (Blog) object;
            return String.valueOf(blog.getBlogId()).equals(
                    String.valueOf(this.getBlogId()));
        } else {
            return super.equals(object);
        }
    }

}
