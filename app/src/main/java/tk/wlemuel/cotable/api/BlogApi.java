package tk.wlemuel.cotable.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

import tk.wlemuel.cotable.core.AppConfig;
import tk.wlemuel.cotable.utils.TLog;

/**
 * BlogApi
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogApi
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public class BlogApi extends BaseApi {

    public static final String TAG = BlogApi.class.getSimpleName();

    private static final String TEMP = "http://cnblogs.davismy.com/Handler" +
            ".ashx?op=GetBlogContent&blog_id={0}";

    public static void getBlogList(int page, AsyncHttpResponseHandler handler) {

        String relativeUrl = RECENT_BLOGS_PAGED.replace(FLAG_PAGEINDEX, Integer.toString(page))
                .replace(FLAG_PAGESIZE, Integer.toString(AppConfig.DEFAULT_PAGE_SIZE));

        TLog.log("Start request data with the method of getBlogList");

        ApiHttpClient.resetClientHost();

        ApiHttpClient.get(relativeUrl, handler);
    }

    public static void getRecommendBlogList(int page, AsyncHttpResponseHandler handler) {

        String partUrl = RECOMMEND_BLOGS_PAGED.replace(FLAG_PAGEINDEX, Integer.toString(page))
                .replace(FLAG_PAGESIZE, Integer.toString(AppConfig.DEFAULT_PAGE_SIZE));

        ApiHttpClient.get(partUrl, handler);
    }

    public static void getBlogDetail(String postId, AsyncHttpResponseHandler handler) {
//        String partUrl = BLOGS_CONTENTS.replace(FLAG_POSTID, postId);

        String partUrl = TEMP.replace("{0}", postId);

        ApiHttpClient.setClientHost("cnblogs.davismy.com");

        ApiHttpClient.getDirect(partUrl, handler);
    }
}
