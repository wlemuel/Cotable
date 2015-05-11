package tk.wlemuel.cotable.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.MySwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.cache.CacheManager;
import tk.wlemuel.cotable.common.AppContext;
import tk.wlemuel.cotable.model.BlogList;
import tk.wlemuel.cotable.model.ListEntity;
import tk.wlemuel.cotable.ui.decorator.DividerItemDecoration;
import tk.wlemuel.cotable.ui.empty.EmptyLayout;
import tk.wlemuel.cotable.ui.widget.FixedRecyclerView;
import tk.wlemuel.cotable.utils.TDevice;

/**
 * BaseRecycleViewFragment
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseRecycleViewFragment
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public abstract class BaseRecycleViewFragment extends BaseTabFragment implements
        BaseRecycleAdapter.OnItemClickListener,
        BaseRecycleAdapter.OnItemLongClickListener {

    public static final String TAG = BaseRecycleViewFragment.class.getSimpleName();

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    protected MySwipeRefreshLayout mSwipeRefresh;
    protected FixedRecyclerView mRecycleView;
    protected LinearLayoutManager mLayoutManager;
    protected BaseRecycleAdapter mAdapter;
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;
    protected String mStoreEmptyMessage;

    protected int mCurrentPage = 0;
    protected int mCatalog = BlogList.CATALOG_LASTEST;

    private AsyncTask<String, Void, ListEntity> mCacheTask;
    private ParserTask mParserTask;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int totalItemCount = mLayoutManager.getItemCount();
            if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                if (mState == STATE_NONE && mAdapter != null
                        && mAdapter.getDataSize() > 0) {
                    loadMore();
                }
            }
        }
    };

    protected int getLayoutRes(){
        return R.layout.fragment_swipe_refresh_recycleview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) mCatalog = args.getInt(BUNDLE_KEY_CATALOG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        initView(view);
        return view;
    }

    protected void initView(View view){
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });

        mSwipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.main);
        mSwipeRefresh.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mRecycleView = (FixedRecyclerView) view.findViewById(R.id.recycleView);
        mRecycleView.setOnScrollListener(mScrollListener);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setHasFixedSize(true);

        if (mAdapter != null) {
            mRecycleView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }else{
            mAdapter = getListAdapter();
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemLongClickListener(this);
            mRecycleView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(requestDataFromNetWork());
            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
        if(!TextUtils.isEmpty(mStoreEmptyMessage)){
            mErrorLayout.setErrorMessage(mStoreEmptyMessage);
        }

    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        mStoreEmptyMessage = mErrorLayout.getMessage();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadCacheTask();
        cancelParserTask();
        super.onDestroy();
    }

    public void loadMore() {
        if (mState == STATE_NONE) {
            if (mAdapter.getState() == BaseListAdapter.STATE_LOAD_MORE) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestData(false);
            }
        }
    }

    protected abstract BaseRecycleAdapter getListAdapter();
    protected boolean requestDataIfViewCreated() {
        return true;
    }

    protected boolean requestDataFromNetWork() {
        return false;
    }

    protected String getCacheKeyPrefix() {
        return null;
    }

    protected String getCacheKey() {
        return new StringBuffer(getCacheKeyPrefix()).append(mCatalog)
                .append("_").append(mCurrentPage).append("_")
                .append(TDevice.getPageSize()).toString();
    }

    protected void requestData(boolean refresh){
        String key = getCacheKey();
        if (TDevice.hasInternet()
                && (!CacheManager.isReadDataCache(getActivity(), key) || refresh)) {
            sendRequestData();
        } else {
            readCacheData(key);
        }
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(this).execute(cacheKey);
    }

    protected void sendRequestData(){

    }

    protected ListEntity parseList(String data) throws Exception {
        return null;
    }

    protected ListEntity readList(Serializable seri) {
        return null;
    }

    public void refresh() {
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);
    }

    protected void executeOnLoadDataSuccess(List<?> data) {
        if (mState == STATE_REFRESH)
            mAdapter.clear();
        mAdapter.addData(data);
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (data.size() == 0 && mState == STATE_REFRESH) {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        } else if (data.size() < TDevice.getPageSize()) {
            if (mState == STATE_REFRESH)
                mAdapter.setState(BaseListAdapter.STATE_NO_MORE);
            else
                mAdapter.setState(BaseListAdapter.STATE_NO_MORE);
        } else {
            mAdapter.setState(BaseListAdapter.STATE_LOAD_MORE);
        }
    }

    protected void onRefreshNetworkSuccess() {
        // TODO do nothing
    }

    protected void executeOnLoadDataError(String error) {
        if (mCurrentPage == 0) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(BaseListAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void executeOnLoadFinish() {
        mSwipeRefresh.setRefreshing(false);
        mState = STATE_NONE;
    }

    protected AsyncHttpResponseHandler getResponseHandler() {
        return new ResponseHandler(this);
    }

    private static class CacheTask extends AsyncTask<String, Void, ListEntity> {
        private WeakReference<BaseRecycleViewFragment> mInstance;

        private CacheTask(BaseRecycleViewFragment instance) {
            mInstance = new WeakReference<BaseRecycleViewFragment>(instance);
        }

        @Override
        protected ListEntity doInBackground(String... params) {
            BaseRecycleViewFragment instance = mInstance.get();
            if (instance != null) {
                Serializable seri = CacheManager.readObject(instance.getActivity(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return instance.readList(seri);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ListEntity list) {
            super.onPostExecute(list);
            BaseRecycleViewFragment instance = mInstance.get();
            if (instance != null) {
                if (list != null) {
                    instance.executeOnLoadDataSuccess(list.getList());
                } else {
                    instance.executeOnLoadDataError(null);
                }
                instance.executeOnLoadFinish();
            }
        }
    }



    private static class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<BaseRecycleViewFragment> mInstance;
        private Serializable seri;
        private String key;

        private SaveCacheTask(BaseRecycleViewFragment instance, Serializable seri, String key) {
            mInstance = new WeakReference<BaseRecycleViewFragment>(instance);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            BaseRecycleViewFragment instance = mInstance.get();
            if (instance != null) {
                CacheManager.saveObject(instance.getActivity(), seri, key);
            }
            return null;
        }
    }

    private static class ResponseHandler extends TextHttpResponseHandler{
        private WeakReference<BaseRecycleViewFragment> mInstance;

        ResponseHandler(BaseRecycleViewFragment instance) {
            mInstance = new WeakReference<BaseRecycleViewFragment>(instance);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            if (mInstance != null) {
                BaseRecycleViewFragment instance = mInstance.get();
                if (instance.isAdded()) {
                    if (instance.mState == STATE_REFRESH) {
                        instance.onRefreshNetworkSuccess();
                        AppContext.setRefreshTime(instance.getCacheKey(),
                                System.currentTimeMillis());
                    }
                    instance.executeParserTask(responseString);
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            if (mInstance != null) {
                BaseRecycleViewFragment instance = mInstance.get();
                if (instance.isAdded()) {
                    instance.readCacheData(instance.getCacheKey());
                }
            }
        }
    }


    // Parse model when request data success.
    private static class ParserTask extends AsyncTask<Void, Void, String> {
        private WeakReference<BaseRecycleViewFragment> mInstance;
        private String reponseData;
        private boolean parserError;
        private List<?> list;

        public ParserTask(BaseRecycleViewFragment instance, String data) {
            this.mInstance = new WeakReference<BaseRecycleViewFragment>(instance);
            this.reponseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            BaseRecycleViewFragment instance = mInstance.get();
            if (instance == null) return null;
            try {
                ListEntity data = instance.parseList(
                        reponseData);
//                if (data instanceof Base) {
//                    Notice notice = ((Base) data).getNotice();
//                    if (notice != null) {
//                        UIHelper.sendBroadCast(instance.getActivity(), notice);
//                    }
//                }
                new SaveCacheTask(instance, data, instance.getCacheKey()).execute();
                list = data.getList();
            } catch (Exception e) {
                e.printStackTrace();
                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            BaseRecycleViewFragment instance = mInstance.get();
            if (instance != null) {
                if (parserError) {
                    instance.readCacheData(instance.getCacheKey());
                } else {
                    instance.executeOnLoadDataSuccess(list);
                    instance.executeOnLoadFinish();
                }
            }
        }
    }

    private void executeParserTask(String data) {
        cancelParserTask();
        mParserTask = new ParserTask(this,data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    @Override
    public void onItemClick(View view) {
        onItemClick(view, mRecycleView.getChildPosition(view));
    }

    protected void onItemClick(View view, int position) {
    }

    @Override
    public boolean onItemLongClick(View view) {
        return onItemLongClick(view, mRecycleView.getChildPosition(view));
    }

    protected boolean onItemLongClick(View view, int position) {
        return false;
    }

}
