package tk.wlemuel.cotable.utils;

import android.content.Context;
import android.content.Intent;

import tk.wlemuel.cotable.activity.blog.view.DetailActivity;

/**
 * UIHelper
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc UIHelper
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class UIHelper {

    /**
     * Show Blog Details
     *
     * @param context context
     * @param blogId  blog id
     */
    public static void showBlogDetail(Context context, int blogId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("blog_id", blogId);
        context.startActivity(intent);
    }


}
