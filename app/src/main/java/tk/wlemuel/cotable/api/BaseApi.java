package tk.wlemuel.cotable.api;

import tk.wlemuel.cotable.core.AppConfig;

/**
 * BaseApi
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseApi
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class BaseApi {

    public static final String TAG = BaseApi.class.getSimpleName();

    public static final String HOST = "cnblogs.davismy.com";
    public static final String DEFAULT_API_URL = "cnblogs.davismy.com/Handler.ashx?op=%s";

    protected static final String UTF8 = AppConfig.UTF8;
    protected final static String FLAG_ITEMCOUNT = "{ITEMCOUNT}";
    protected final static String FLAG_CONTENTID = "{CONTENTID}";
    protected final static String FLAG_PAGEINDEX = "{PAGEINDEX}";
    protected final static String FLAG_PAGESIZE = "{PAGESIZE}";
    protected final static String FLAG_BLOGAPP = "{BLOGAPP}";
    protected final static String FLAG_POSTID = "{POSTID}";
    protected final static String FLAG_TERM = "{TERM}";
    // Api (NEWS)


    // Api (BLOG)
    protected final static String RECOMMEND_BLOGS_PAGED = "GetTimeLine&page={PAGEINDEX}";
    protected final static String RECENT_BLOGS_PAGED = "GetTimeLine&page={PAGEINDEX}";
    protected final static String BLOG_CONTENTS = "GetBlogContent&blog_id={POSTID}";

    protected final static String GET_USER_INFO = "GetUserInfo&blogapp={BLOGAPP}";

    protected static String API_URL = DEFAULT_API_URL;


}
