package tk.wlemuel.cotable.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.utils.TDevice;
import tk.wlemuel.cotable.utils.TLog;

/**
 * BaseRecycleAdapter
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseRecycleAdapter
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public abstract class BaseRecycleAdapter
        extends RecyclerView.Adapter<BaseRecycleAdapter.ViewHolder> {

    public static final String TAG = BaseRecycleAdapter.class.getSimpleName();

    public static final int STATE_EMPTY_ITEM = 0;
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_NO_DATA = 3;
    public static final int STATE_LESS_ONE_PAGE = 4;
    public static final int STATE_NETWORK_ERROR = 5;

    public static final int TYPE_FOOTER = 0x101;
    public static final int TYPE_HEADER = 0x102;

    protected int state = STATE_LESS_ONE_PAGE;

    protected int _loadmoreText;
    protected int _loadFinishText;

    private LayoutInflater mInflater;

    protected List mData;

    private WeakReference<OnItemClickListener> mListener;
    private WeakReference<OnItemLongClickListener> mLongListener;
    private View mHeaderView;

    public BaseRecycleAdapter() {
        _loadmoreText = R.string.loading;
        _loadFinishText = R.string.loading_no_more;

        mData = new ArrayList();

        init();

    }

    protected void init() {

    }

    public int getDataSize() {
        return mData.size();
    }

    private Boolean hasHeader() {
        return mHeaderView != null;
    }

    private Boolean hasFooter() {
        switch (getState()) {
            case STATE_EMPTY_ITEM:
            case STATE_LOAD_MORE:
            case STATE_NO_MORE:
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        int size = getDataSize();
        if (hasFooter()) size += 1;

        if (hasHeader()) size += 1;

        TLog.log("Get Item Count: " + size + " Data Size: " + getDataSize());

        return size;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public Object getItem(int arg){
        if(arg < 0) return null;
        if(mData.size() > arg) return mData.get(arg);

        return null;
    }

    public void addData(List data){
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public List getData(){
        if(mData == null) mData = new ArrayList();

        return mData;
    }

    public void addItem(int pos, Object obj){
        getData();
        mData.add(pos, obj);
        notifyDataSetChanged();
    }

    public void addItem(Object obj){
        getData();
        mData.add(obj);
        notifyDataSetChanged();
    }

    public void removeItem(Object obj){
        if(mData != null){
            mData.remove(obj);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int pos){
        if(mData != null){
            mData.remove(pos);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void setLoadmoreText(int text){
        _loadmoreText = text;
    }

    public void setLoadFinishText(int text){
        _loadFinishText = text;
    }

    protected boolean loadMoreHasBg(){
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = new WeakReference<OnItemClickListener>(listener);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = new WeakReference<OnItemLongClickListener>(listener);
    }

    public void setHeaderView(View v){
        mHeaderView = v;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && hasHeader())
            return TYPE_HEADER;
        if(position == getItemCount() - 1 && hasFooter())
            return TYPE_FOOTER;

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        if(viewType == TYPE_FOOTER){
            View v = getLayoutInflater(parent.getContext())
                    .inflate(R.layout.list_cell_footer, null);
            vh = new FooterViewHolder(viewType, v);
        } else if (viewType == TYPE_HEADER){
            if(mHeaderView == null)
                throw new RuntimeException("Header view is null");
            vh = new HeaderViewHolder(viewType, mHeaderView);
        } else {
            final View itemView = onCreateItemView(parent, viewType);
            if(itemView != null){
                if(mListener != null){
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OnItemClickListener oil = mListener.get();
                            if(oil != null)
                                oil.onItemClick(itemView);
                        }
                    });
                }

                if(mLongListener != null){
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            OnItemLongClickListener oil = mLongListener.get();
                            if (oil != null)
                                return oil.onItemLongClick(itemView);
                            return false;
                        }
                    });
                }
            }
            vh = onCreateItemViewHolder(itemView, viewType);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if ((getItemViewType(position) == TYPE_HEADER && position == 0)
                || holder instanceof HeaderViewHolder){
            TLog.log("Bind head view: " + position + " " + holder.viewType);
            onBindHeadViewHolder(holder, position);
        } else if((getItemViewType(position) == TYPE_FOOTER && position == getItemCount() - 1)
                || holder instanceof FooterViewHolder){
            TLog.log("Bind foot view: " + position + " " + holder.viewType);
            onBindFootViewHolder(holder, position);
        } else {
            TLog.log("Bind item view" + position + " " + holder.viewType);
            onBindItemViewHolder(holder, position);
        }
    }

    protected void onBindHeadViewHolder(ViewHolder holder, int position){

    }

    protected void onBindItemViewHolder(ViewHolder holder, int position){

    }

    protected void onBindFootViewHolder(ViewHolder holder, int position){
        FooterViewHolder vh = (FooterViewHolder) holder;
        if(!loadMoreHasBg()) vh.mLoadMore.setBackground(null);

        switch (getState()){
            case STATE_LOAD_MORE:
                vh.mLoadMore.setVisibility(View.VISIBLE);
                vh.mProgressBar.setVisibility(View.VISIBLE);
                vh.mTextView.setVisibility(View.VISIBLE);
                vh.mTextView.setText(_loadmoreText);
                break;
            case STATE_NO_MORE:
                vh.mLoadMore.setVisibility(View.VISIBLE);
                vh.mProgressBar.setVisibility(View.GONE);
                vh.mTextView.setVisibility(View.VISIBLE);
                vh.mTextView.setText(_loadFinishText);
                break;
            case STATE_EMPTY_ITEM:
                vh.mLoadMore.setVisibility(View.GONE);
                vh.mProgressBar.setVisibility(View.GONE);
                vh.mTextView.setVisibility(View.GONE);
                break;
            case STATE_NETWORK_ERROR:
                vh.mLoadMore.setVisibility(View.VISIBLE);
                vh.mProgressBar.setVisibility(View.GONE);
                vh.mTextView.setVisibility(View.VISIBLE);
                if(TDevice.hasInternet()){
                    vh.mTextView.setText(R.string.loading_error);
                }else{
                    vh.mTextView.setText(R.string.loading_no_network);
                }
                break;
            default:
                vh.mLoadMore.setVisibility(View.GONE);
                vh.mProgressBar.setVisibility(View.GONE);
                vh.mTextView.setVisibility(View.GONE);
                break;
        }
    }

    protected abstract View onCreateItemView(ViewGroup parent, int viewType);

    protected abstract ViewHolder onCreateItemViewHolder(View view, int viewType);

    public interface OnItemClickListener {
        void onItemClick(View v);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v);
    }

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null)
            mInflater = (LayoutInflater.from(context));

        return mInflater;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int viewType;

        public ViewHolder(int viewType, View v) {
            super(v);
            this.viewType = viewType;
        }
    }

    public static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(int viewType, View v) {
            super(viewType, v);
        }
    }

    public static class FooterViewHolder extends ViewHolder {
        public ProgressBar mProgressBar;
        public TextView mTextView;
        public View mLoadMore;

        public FooterViewHolder(int viewType, View v) {
            super(viewType, v);
            mLoadMore = v;
            mProgressBar = (ProgressBar) v.findViewById(R.id.progressbar);
            mTextView = (TextView) v.findViewById(R.id.hint);
        }
    }
}
