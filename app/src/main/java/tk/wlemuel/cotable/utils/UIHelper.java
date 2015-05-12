package tk.wlemuel.cotable.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import tk.wlemuel.cotable.activity.blog.fragment.BlogFragment;
import tk.wlemuel.cotable.activity.common.SimpleBackActivity;
import tk.wlemuel.cotable.model.SimpleBackPage;

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
    public static void showBlogDetail(Context context, String blogId) {
        Bundle args = new Bundle();
        args.putString(BlogFragment.BUNDLE_KEY_BLOG_ID, blogId);
        showSimpleBack(context, SimpleBackPage.DETAILS, args);
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

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());

        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }


}
