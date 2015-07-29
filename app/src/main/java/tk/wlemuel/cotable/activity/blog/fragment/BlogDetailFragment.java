package tk.wlemuel.cotable.activity.blog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import java.io.Serializable;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.activity.view.CoScrollView;
import tk.wlemuel.cotable.activity.view.ReaderWebView;
import tk.wlemuel.cotable.activity.view.ScrollDirectionListener;
import tk.wlemuel.cotable.api.BlogApi;
import tk.wlemuel.cotable.base.BaseDetailFragment;
import tk.wlemuel.cotable.core.AppConfig;
import tk.wlemuel.cotable.core.AppContext;
import tk.wlemuel.cotable.model.BlogDetail;
import tk.wlemuel.cotable.model.Entity;
import tk.wlemuel.cotable.ui.empty.EmptyLayout;
import tk.wlemuel.cotable.utils.BlogReaderAnim;
import tk.wlemuel.cotable.utils.StringUtils;

/**
 * BlogDetailFragment
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogDetailFragment
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class BlogDetailFragment extends BaseDetailFragment implements
        ScrollDirectionListener,
        ReaderWebView.ReaderWebViewPageFinishedListener,
        ReaderWebView.ReaderCustomViewListener,
        ReaderWebView.ReaderWebViewUrlClickListener {

    public static final String BUNDLE_KEY_BLOG_ID = "BLOG_ID";
    public static final String BUNDLE_KEY_BLOG_TITLE = "BLOG_TITLE";
    public static final String BUNDLE_KEY_BLOG_AUTHOR = "BLOG_AUTHOR";
    public static final String BUNDLE_KEY_BLOG_READS = "BLOG_READS";
    public static final String BUNDLE_KEY_BLOG_POSTED = "BLOG_POSTED";

    protected static final String TAG = BlogDetailFragment.class.getSimpleName();
    private static final String BLOG_CACHE_KEY = "blog_";
    private static final String CONTENT_TEMPLATE = "content-template.html";
    private static final String mimeType = "text/html";
    private String mBlogId;
    private String mBlogTitle;
    private String mBlogAuthor;
    private String mBlogReads;
    private String mBlogPosted;
    private BlogDetail mBlogDetail;

    private RelativeLayout layout_actions;

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
            mBlogTitle = (String) args.get(BUNDLE_KEY_BLOG_TITLE);
            mBlogAuthor = (String) args.get(BUNDLE_KEY_BLOG_AUTHOR);
            mBlogPosted = (String) args.get(BUNDLE_KEY_BLOG_POSTED);
            mBlogReads = (String) args.get(BUNDLE_KEY_BLOG_READS);
        }

        if (mBlogId == null || mBlogId.equals("")) {
            mBlogId = "4495118";
        }

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mScrollView = (CoScrollView) view.findViewById(R.id.sv_blog_container);
        mScrollView.setScrollDirectionListener(this);
        mWebView = (ReaderWebView)view.findViewById(R.id.webview);
        layout_actions = (RelativeLayout)view.findViewById(R.id.layout_actions);
        initWebView(mWebView);
        mWebView.setPageFinishedListener(this);
        layout_actions.setVisibility(View.INVISIBLE);
        view.findViewById(R.id.ib_zoomin).setOnClickListener(this);
        view.findViewById(R.id.ib_zoomout).setOnClickListener(this);
    }

    @Override
    public void onPageFinished(WebView view, String url,boolean receiveError) {
        if (!isAdded()) {
            return;
        }
        if (mEmptyLayout != null) {
            if (receiveError) {
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            } else {
                mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }
    }



    @Override
    public void onScrollUp() {
        showIconBar(true);
        // showToolbar(true); 显示ToolBar
    }

    private void showIconBar(boolean show) {
        if (isAdded() && canShowIconBar()) {
            BlogReaderAnim.animateBottomBar(layout_actions, show);
        }
    }

    private boolean canShowIconBar() {
        if (mBlogDetail == null || StringUtils.isNullOrEmpty(mBlogDetail.getBody())) {
            return false;
        }
//        return (mBlogDetail.isLikesEnabled()
//                || mBlogDetail.canReply()
//                || mBlogDetail.isCommentsOpen());  //判断博客是否能被点赞、回复、评论
        return true;
    }



    @Override
    public void onScrollDown() {
        if (mScrollView.canScrollDown() && mScrollView.canScrollUp()) {
            showIconBar(false);
           // showToolbar(false);
        }
    }

    @Override
    public void onScrollCompleted() {
        if (!mScrollView.canScrollDown()) {
            showIconBar(true);
        }
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

        String content = AppContext.getFromAssets(CONTENT_TEMPLATE);
        if (content == null) content = "";

        content = content.replace("{%skin%}", "day");
        content = content.replace("{%title%}", mBlogTitle);
        content = content.replace("{%author%}", mBlogAuthor);
        content = content.replace("{%posted%}", mBlogPosted);
        content = content.replace("{%reads%}", mBlogReads);
        content = content.replace("{%Content%}", mBlogDetail.getBody());

        mWebView.loadDataWithBaseURL(
                AppConfig.LOCAL_PATH, content, mimeType, AppConfig.UTF8, null);
        layout_actions.setVisibility(View.VISIBLE);
    }

    /*
    * 点击WebView的URl地址事件
    * */
    @Override
    public boolean onUrlClick(String url) {

        return true;
    }
    /*
    * 点击WebView的图片事件
    *
    * */
    @Override
    public boolean onImageUrlClick(String imageUrl, View view, int x, int y) {
        //点击WebView的图片
        return true;
    }


    /*
    * 关于视频的点击操作事件
    */
    @Override
    public ViewGroup onRequestCustomView() {
        return null;
    }

    /*
     * 关于视频的点击操作事件
     */
    @Override
    public ViewGroup onRequestContentView() {
        return null;
    }

    @Override
    public void onCustomViewShown() {

    }

    @Override
    public void onCustomViewHidden() {

    }


}
