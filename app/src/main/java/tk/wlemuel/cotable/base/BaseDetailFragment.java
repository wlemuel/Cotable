package tk.wlemuel.cotable.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import tk.wlemuel.cotable.ui.empty.EmptyLayout;

/**
 * BaseDetailFragment
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseDetailFragment
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public abstract class BaseDetailFragment extends BaseFragment {

    protected EmptyLayout mEmptyLayout;
    protected WebView mWebView;

    protected WebViewClient mWebViewClient = new WebViewClient() {
        private boolean receiveError = false;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            receiveError = false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mEmptyLayout != null) {
                if (receiveError) {
                    mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                } else {
                    mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                }
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            receiveError = true;
        }

    };

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }

    }


}
