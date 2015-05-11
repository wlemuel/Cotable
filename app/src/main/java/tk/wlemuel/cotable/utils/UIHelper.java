package tk.wlemuel.cotable.utils;

import android.app.Activity;
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

    /**
     * Call the system application with shares.
     *
     * @param context context
     * @param title   share title
     * @param url     share url
     */
    public static void showShareMore(Activity context, final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share: " + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "Choose to share"));
    }


}
