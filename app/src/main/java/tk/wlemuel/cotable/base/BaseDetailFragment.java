package tk.wlemuel.cotable.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import tk.wlemuel.cotable.cache.CacheManager;
import tk.wlemuel.cotable.model.Entity;
import tk.wlemuel.cotable.ui.empty.EmptyLayout;
import tk.wlemuel.cotable.utils.TDevice;

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
    private AsyncTask<String, Void, Entity> mCacheTask;
    protected AsyncHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            readCacheData(getCacheKey());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Entity entity = parseData(responseString);
                if (entity != null) {
                    executeOnLoadDataSuccess(entity);
                    saveCache(entity);
                } else {
                    throw new RuntimeException("load detail error.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
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

    protected void recycleWebView() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onDestroyView() {
        recycleWebView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadCache();
        recycleWebView();
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData(false);
    }

    protected String getCacheKey() {
        return null;
    }

    protected Entity parseData(String data) throws Exception {
        return null;
    }

    protected Entity readData(Serializable seri) {
        return null;
    }

    protected void sendRequestData() {
    }

    protected void requestData(boolean refresh) {
        String key = getCacheKey();
        if (TDevice.hasInternet()
                && (!CacheManager.isReadDataCache(getActivity(), key) || refresh)) {
            sendRequestData();
        } else {
            readCacheData(key);
        }
    }

    private void readCacheData(String cacheKey) {
        cancelReadCache();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCache() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    protected void saveCache(Entity entity) {
        new SaveCacheTask(getActivity(), entity, getCacheKey()).execute();
    }

    protected void executeOnLoadDataSuccess(Entity entity) {

    }

    protected void executeOnLoadDataError(String object) {
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mState = STATE_REFRESH;
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });
    }

    protected void executeOnLoadFinish() {
    }

    private class CacheTask extends AsyncTask<String, Void, Entity> {
        private WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected Entity doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return readData(seri);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Entity entity) {
            super.onPostExecute(entity);
            if (entity != null) {
                executeOnLoadDataSuccess(entity);
            } else {
                executeOnLoadDataError(null);
            }
            executeOnLoadFinish();
        }
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> mContext;
        private Serializable seri;
        private String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }
}
