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

    public static void getBlogList(int page, AsyncHttpResponseHandler handler) {

        String relativeUrl = RECENT_BLOGS_PAGED.replace(FLAG_PAGEINDEX, Integer.toString(page))
                .replace(FLAG_PAGESIZE, Integer.toString(AppConfig.DEFAULT_PAGE_SIZE));

        TLog.log("Start request data with the method of getBlogList");

        ApiHttpClient.get(relativeUrl, handler);
    }

    public static void getRecommendBlogList(int page, AsyncHttpResponseHandler handler) {

        String partUrl = RECOMMEND_BLOGS_PAGED.replace(FLAG_PAGEINDEX, Integer.toString(page))
                .replace(FLAG_PAGESIZE, Integer.toString(AppConfig.DEFAULT_PAGE_SIZE));

        ApiHttpClient.get(partUrl, handler);
    }

    public static void getBlogDetail(String postId, AsyncHttpResponseHandler handler) {
        String partUrl = BLOG_CONTENTS.replace(FLAG_POSTID, postId);

        ApiHttpClient.get(partUrl, handler);
    }

    public static void getAuthorAvatar(String blogapp, AsyncHttpResponseHandler handler) {
        String partUrl = GET_USER_INFO.replace(FLAG_BLOGAPP, blogapp);

        ApiHttpClient.get(partUrl, handler);
    }
}
