package tk.wlemuel.cotable.activity.blog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.Serializable;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.api.BlogApi;
import tk.wlemuel.cotable.base.BaseDetailFragment;
import tk.wlemuel.cotable.core.AppConfig;
import tk.wlemuel.cotable.core.AppContext;
import tk.wlemuel.cotable.model.BlogDetail;
import tk.wlemuel.cotable.model.Entity;
import tk.wlemuel.cotable.ui.empty.EmptyLayout;

/**
 * BlogFragment
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogFragment
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class BlogFragment extends BaseDetailFragment {

    public static final String BUNDLE_KEY_BLOG_ID = "BLOG_ID";
    protected static final String TAG = BlogFragment.class.getSimpleName();
    private static final String BLOG_CACHE_KEY = "blog_";
    private static final String CONTENT_TEMPLATE = "content.html";
    private static final String mimeType = "text/html";
    private String mBlogId;
    private BlogDetail mBlogDetail;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        if (id == R.id.ib_zoomin) {
            mWebView.zoomIn();
        } else if (id == R.id.ib_zoomout) {
            mWebView.zoomOut();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_detail, container, false);

        Bundle args = getArguments();
        if (args != null) {
            mBlogId = (String) args.get(BUNDLE_KEY_BLOG_ID);
        }

        if (mBlogId == null || mBlogId.equals("")) {
            mBlogId = "4495118";
        }

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mWebView = (WebView) view.findViewById(R.id.webview);

        initWebView(mWebView);

        view.findViewById(R.id.ib_zoomin).setOnClickListener(this);
        view.findViewById(R.id.ib_zoomout).setOnClickListener(this);
    }

    @Override
    protected String getCacheKey() {
        return BLOG_CACHE_KEY + mBlogId;
    }

    @Override
    protected void sendRequestData() {
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        BlogApi.getBlogDetail(mBlogId, mHandler);
    }

    @Override
    protected Entity parseData(String data) throws Exception {
        return BlogDetail.parse(data);
    }

    @Override
    protected Entity readData(Serializable seri) {
        return (BlogDetail) seri;
    }

    @Override
    protected void executeOnLoadDataSuccess(Entity entity) {
        mBlogDetail = (BlogDetail) entity;
        fillWebViewBody();
    }

    private void fillWebViewBody() {
        mWebView.setWebViewClient(mWebViewClient);

        String content = AppContext.getFromAssets(CONTENT_TEMPLATE);
        if (content == null) content = "";

        content = content.replace("{%skin%}", "day");
        content = content.replace("{%size%}", "font-middle");
        content = content.replace("{%Content%}", mBlogDetail.getBody());

        mWebView.loadDataWithBaseURL(
                AppConfig.LOCAL_PATH, content, mimeType, AppConfig.UTF8, null);
    }
}
