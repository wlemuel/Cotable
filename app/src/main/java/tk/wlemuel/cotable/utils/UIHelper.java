package tk.wlemuel.cotable.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import java.util.Map;

import tk.wlemuel.cotable.activity.blog.view.DetailActivity;
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
     * @param blogInfo  blog id
     */
    public static void showBlogDetail(Context context, Map<String, String> blogInfo) {
        Bundle args = new Bundle();

        if (blogInfo != null) {
            for (Map.Entry<String, String> entry : blogInfo.entrySet()) {
                args.putString(entry.getKey(), entry.getValue());
            }
        }

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
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    /**
     * 添加网页的点击图片展示支持
     */
    @SuppressLint("JavascriptInterface")
    public static void addWebImageShow(final Context cxt, WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new OnWebViewImageListener() {

            @Override
            public void onImageClick(String bigImageUrl) {
                if (bigImageUrl != null) {
                    // UIHelper.showImageZoomDialog(cxt, bigImageUrl);
//                    UIHelper.showImagePreview(cxt, new String[]{bigImageUrl});
                }
            }
        }, "mWebViewImageListener");
    }


}
