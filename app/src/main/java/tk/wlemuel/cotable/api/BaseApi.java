package tk.wlemuel.cotable.api;

import tk.wlemuel.cotable.common.AppConfig;

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

    protected static final String DEFAULT_API_URL = "wcf.open.cnblogs.com";
    protected static String API_URL = DEFAULT_API_URL;
    protected static final String UTF8 = AppConfig.UTF8;

    protected final static String FLAG_ITEMCOUNT = "{ITEMCOUNT}";
    protected final static String FLAG_CONTENTID = "{CONTENTID}";
    protected final static String FLAG_PAGEINDEX = "{PAGEINDEX}";
    protected final static String FLAG_PAGESIZE = "{PAGESIZE}";
    protected final static String FLAG_BLOGAPP = "{BLOGAPP}";
    protected final static String FLAG_POSTID = "{POSTID}";
    protected final static String FLAG_TERM = "{TERM}";

    // Api (NEWS)
    // 获取新闻列表
    protected final static String NEWS_LIST = "news/GetData";
    // 获取热门新闻列表
    protected final static String HOT_NEWS_LIST = "news/hot/{ITEMCOUNT}";
    // 获取新闻内容
    protected final static String NEWS_CONTENT = "news/item/{CONTENTID}";
    // 获取新闻评论
    protected final static String NEWS_COMMENTS = "news/item/{CONTENTID}/comments/{PAGEINDEX}/{PAGESIZE}";
    // 获取最新新闻列表
    protected final static String RECENT_NEWS = "news/recent/{ITEMCOUNT}";
    // 分页获取最新新闻列表
    protected final static String RECENT_NEWS_PAGED = "news/recent/paged/{PAGEINDEX}/{PAGESIZE}";
    // 分页获取推荐新闻列表
    protected final static String RECOMMEND_NEWS_PAGED = "news/recommend/paged/{PAGEINDEX}/{PAGESIZE}";

    // Api (BLOG)
    // 48小时阅读排行
    protected final static String TOP_VIEW_POSTS_48_HOUR = "blog/48HoursTopViewPosts/{ITEMCOUNT}";
    // 分页获取推荐博客列表(作者)
    protected final static String RECOMMEND_BLOGS_PAGED = "blog/bloggers/recommend/{PAGEINDEX}/{PAGESIZE}";
    // 获取推荐博客总数
    protected final static String RECOMMEND_BLOGS_COUNT = "blog/bloggers/recommend/count";
    // 根据作者名搜索博主
    protected final static String SEARCH_AUTHOR_BY_NAME = "blog/bloggers/search?t={TERM}";
    // 获取文章评论
    protected final static String BLOGS_COMMENTS = "blog/post/{POSTID}/comments/{PAGEINDEX}/{PAGESIZE}";
    // 获取文章内容
    protected final static String BLOGS_CONTENTS = "blog/post/body/{POSTID}";
    // 获取首页文章列表
    protected final static String RECENT_BLOGS = "blog/sitehome/recent/{ITEMCOUNT}";
    // 分页获取首页文章列表
    protected final static String RECENT_BLOGS_PAGED = "blog/sitehome/paged/{PAGEINDEX}/{PAGESIZE}";
    // 10天内推荐排行
    protected final static String TEN_DAYS_TOP_DIGG_POSTS = "blog/TenDaysTopDiggPosts/{ITEMCOUNT}";
    // 分页获取个人博客文章列表
    protected final static String USER_BLOGS_LIST_PAGED = "blog/u/{BLOGAPP}/posts/{PAGEINDEX}/{PAGESIZE}";


}
