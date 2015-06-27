package tk.wlemuel.cotable.activity.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import tk.wlemuel.cotable.ui.empty.EmptyLayout;

/**
 * Created by Johnnyz Zhang on 2015/6/26.
 */
public class ReaderWebView extends WebView {

    public interface ReaderWebViewUrlClickListener {
        boolean onUrlClick(String url);
        boolean onImageUrlClick(String imageUrl, View view, int x, int y);
    }

    public interface ReaderCustomViewListener {
        void onCustomViewShown();
        void onCustomViewHidden();
        ViewGroup onRequestCustomView();
        ViewGroup onRequestContentView();
    }

    public interface ReaderWebViewPageFinishedListener {
        void onPageFinished(WebView view, String url,boolean receiveError);
    }

    private ReaderWebChromeClient mReaderChromeClient;
    private ReaderCustomViewListener mCustomViewListener;
    private ReaderWebViewUrlClickListener mUrlClickListener;
    private ReaderWebViewPageFinishedListener mPageFinishedListener;


    private boolean mIsDestroyed;


    public ReaderWebView(Context context) {
        super(context);
        init();
    }

    @Override
    public void destroy() {
        mIsDestroyed = true;
        super.destroy();
    }

    public boolean isDestroyed() {
        return mIsDestroyed;
    }

    public ReaderWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReaderWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        if (!isInEditMode()) {
            mReaderChromeClient = new ReaderWebChromeClient(this);
            this.setWebChromeClient(mReaderChromeClient);
            this.setWebViewClient(new ReaderWebViewClient(this));
            // Lollipop disables third-party cookies by default, but we need them in order
            // to support authenticated images
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
            }
        }
    }

    private ReaderWebViewUrlClickListener getUrlClickListener() {
        return mUrlClickListener;
    }

    public void setUrlClickListener(ReaderWebViewUrlClickListener listener) {
        mUrlClickListener = listener;
    }

    private boolean hasUrlClickListener() {
        return (mUrlClickListener != null);
    }

    private ReaderWebViewPageFinishedListener getPageFinishedListener() {
        return mPageFinishedListener;
    }

    public void setPageFinishedListener(ReaderWebViewPageFinishedListener listener) {
        mPageFinishedListener = listener;
    }

    private boolean hasPageFinishedListener() {
        return (mPageFinishedListener != null);
    }

    public void setCustomViewListener(ReaderCustomViewListener listener) {
        mCustomViewListener = listener;
    }

    private boolean hasCustomViewListener() {
        return (mCustomViewListener != null);
    }

    private ReaderCustomViewListener getCustomViewListener() {
        return mCustomViewListener;
    }

    private static boolean isValidClickedUrl(String url) {
        // only return true for http(s) urls so we avoid file: and data: clicks
        return (url != null && url.startsWith("http"));
    }

    public boolean isCustomViewShowing() {
        return mReaderChromeClient.isCustomViewShowing();
    }

    public void hideCustomView() {
        if (isCustomViewShowing()) {
            mReaderChromeClient.onHideCustomView();
        }
    }

    /*
     * detect when an image is tapped
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && mUrlClickListener != null) {
            HitTestResult hr = getHitTestResult();
            if (hr != null && (hr.getType() == HitTestResult.IMAGE_TYPE
                    || hr.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {
                String imageUrl = hr.getExtra();
                if (isValidClickedUrl(imageUrl) ) {
                    return mUrlClickListener.onImageUrlClick(
                            imageUrl,
                            this,
                            (int) event.getX(),
                            (int) event.getY());
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private static class ReaderWebViewClient extends WebViewClient {
        private final ReaderWebView mReaderWebView;
        private boolean receiveError;
        ReaderWebViewClient(ReaderWebView readerWebView) {
            if (readerWebView == null) {
                throw new IllegalArgumentException("ReaderWebViewClient requires readerWebView");
            }
            mReaderWebView = readerWebView;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            receiveError = false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            receiveError = true;
        }




        @Override
        public void onPageFinished(WebView view, String url) {
            if (mReaderWebView.hasPageFinishedListener()) {
                mReaderWebView.getPageFinishedListener().onPageFinished(view, url,receiveError);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // fire the url click listener, but only do this when webView has
            // loaded (is visible) - have seen some posts containing iframes
            // automatically try to open urls (without being clicked)
            // before the page has loaded
            if (view.getVisibility() == View.VISIBLE
                    && mReaderWebView.hasUrlClickListener()
                    && isValidClickedUrl(url)) {
                return mReaderWebView.getUrlClickListener().onUrlClick(url);
            } else {
                return false;
            }
        }

//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            if (url != null && url.length() > 0) {
//                try {
//                    URL imageUrl = new URL(url);
//                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//                    conn.setRequestProperty("Connection", "Keep-Alive");
//                    return new WebResourceResponse(conn.getContentType(),
//                            conn.getContentEncoding(),
//                            conn.getInputStream());
//                } catch (IOException e) {
//
//                }
//            }
//
//            return super.shouldInterceptRequest(view, url);
//        }
    }

    private static class ReaderWebChromeClient extends WebChromeClient {
        private final ReaderWebView mReaderWebView;
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;

        ReaderWebChromeClient(ReaderWebView readerWebView) {
            if (readerWebView == null) {
                throw new IllegalArgumentException("ReaderWebChromeClient requires readerWebView");
            }
            mReaderWebView = readerWebView;
        }

        /*
         * request the view that will host the fullscreen video
         */
        private ViewGroup getTargetView() {
            if (mReaderWebView.hasCustomViewListener()) {
                return mReaderWebView.getCustomViewListener().onRequestCustomView();
            } else {
                return null;
            }
        }

        /*
         * request the view that should be hidden when showing fullscreen video
         */
        private ViewGroup getContentView() {
            if (mReaderWebView.hasCustomViewListener()) {
                return mReaderWebView.getCustomViewListener().onRequestContentView();
            } else {
                return null;
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
           Log.i(ReaderWebView.class.getSimpleName(), "onShowCustomView");

            if (mCustomView != null) {
                Log.w(ReaderWebView.class.getSimpleName(), "customView already showing");
                onHideCustomView();
                return;
            }

            // hide the post detail content
            ViewGroup contentView = getContentView();
            if (contentView != null) {
                contentView.setVisibility(View.INVISIBLE);
            }

            // show the full screen view
            ViewGroup targetView = getTargetView();
            if (targetView != null) {
                targetView.addView(view);
                targetView.setVisibility(View.VISIBLE);
            }

            if (mReaderWebView.hasCustomViewListener()) {
                mReaderWebView.getCustomViewListener().onCustomViewShown();
            }

            mCustomView = view;
            mCustomViewCallback = callback;
        }

        @Override
        public void onHideCustomView() {
            Log.i(ReaderWebView.class.getSimpleName(), "onHideCustomView");

            if (mCustomView == null) {
                Log.w(ReaderWebView.class.getSimpleName(), "customView does not exist");
                return;
            }

            // hide the target view
            ViewGroup targetView = getTargetView();
            if (targetView != null) {
                targetView.removeView(mCustomView);
                targetView.setVisibility(View.GONE);
            }

            // redisplay the post detail content
            ViewGroup contentView = getContentView();
            if (contentView != null) {
                contentView.setVisibility(View.VISIBLE);
            }

            if (mCustomViewCallback != null) {
                mCustomViewCallback.onCustomViewHidden();
            }
            if (mReaderWebView.hasCustomViewListener()) {
                mReaderWebView.getCustomViewListener().onCustomViewHidden();
            }

            mCustomView = null;
            mCustomViewCallback = null;

            if(Build.VERSION.SDK_INT > 10){
                mReaderWebView.onPause();
            }
        }

        boolean isCustomViewShowing() {
            return (mCustomView != null);
        }
    }
}

