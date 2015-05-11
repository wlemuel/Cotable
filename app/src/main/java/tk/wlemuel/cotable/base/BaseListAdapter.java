package tk.wlemuel.cotable.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.utils.TDevice;

/**
 * BaseListAdapter
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseListAdapter
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public class BaseListAdapter extends BaseAdapter {
    public static final int STATE_EMPTY_ITEM = 0;
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_NO_DATA = 3;
    public static final int STATE_LESS_ONE_PAGE = 4;
    public static final int STATE_NETWORK_ERROR = 5;

    protected int state = STATE_LESS_ONE_PAGE;

    protected int _loadmoreText;
    protected int _loadFinishText;
    protected int mScreenWidth;
    @SuppressWarnings("rawtypes")
    protected ArrayList _data = new ArrayList();
    private LayoutInflater mInflater;
    private boolean mLoadMoreHasBg = true;

    public BaseListAdapter() {
        _loadmoreText = R.string.loading;
        _loadFinishText = R.string.loading_no_more;
    }

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    public void setScreenWidth(int width) {
        mScreenWidth = width;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int getCount() {
        switch (getState()) {
            case STATE_EMPTY_ITEM:
                return getDataSize() + 1;
            case STATE_NETWORK_ERROR:
            case STATE_LOAD_MORE:
                return getDataSize() + 1;
            case STATE_NO_DATA:
                return 0;
            case STATE_NO_MORE:
                return getDataSize() + 1;
            case STATE_LESS_ONE_PAGE:
                return getDataSize();
            default:
                break;
        }
        return getDataSize();
    }

    public int getDataSize() {
        return _data.size();
    }

    @Override
    public Object getItem(int arg0) {
        if (arg0 < 0)
            return null;
        if (_data.size() > arg0) {
            return _data.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressWarnings("rawtypes")
    public ArrayList getData() {
        return _data == null ? (_data = new ArrayList()) : _data;
    }

    @SuppressWarnings("rawtypes")
    public void setData(ArrayList data) {
        _data = data;
        notifyDataSetChanged();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addData(List data) {
        if (_data == null) {
            _data = new ArrayList();
        }
        _data.addAll(data);
        notifyDataSetChanged();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addItem(Object obj) {
        if (_data == null) {
            _data = new ArrayList();
        }
        _data.add(obj);
        notifyDataSetChanged();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addItem(int pos, Object obj) {
        if (_data == null) {
            _data = new ArrayList();
        }
        _data.add(pos, obj);
        notifyDataSetChanged();
    }

    public void removeItem(Object obj) {
        _data.remove(obj);
        notifyDataSetChanged();
    }

    public void clear() {
        _data.clear();
        notifyDataSetChanged();
    }

    public void setLoadmoreText(int loadmoreText) {
        _loadmoreText = loadmoreText;
    }

    public void setLoadFinishText(int loadFinishText) {
        _loadFinishText = loadFinishText;
    }

    public void setLoadMoreHasBg(boolean flag) {
        this.mLoadMoreHasBg = flag;
    }

    protected boolean loadMoreHasBg() {
        return mLoadMoreHasBg;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {// 最后一条
            if (getState() == STATE_LOAD_MORE || getState() == STATE_NO_MORE
                    || state == STATE_EMPTY_ITEM
                    || getState() == STATE_NETWORK_ERROR) {
                View loadmore = LayoutInflater.from(
                        parent.getContext()).inflate(
                        R.layout.list_cell_footer, null);
                if (!loadMoreHasBg()) {
                    loadmore.setBackgroundColor(parent.getContext().getResources().getColor(R.color.transparent));
                }
                ProgressBar progress = (ProgressBar) loadmore
                        .findViewById(R.id.progressbar);
                TextView text = (TextView) loadmore.findViewById(R.id.hint);
                switch (getState()) {
                    case STATE_LOAD_MORE:
                        loadmore.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        text.setText(_loadmoreText);
                        break;
                    case STATE_NO_MORE:
                        loadmore.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        text.setText(_loadFinishText);
                        break;
                    case STATE_EMPTY_ITEM:
                        progress.setVisibility(View.GONE);
                        loadmore.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                        break;
                    case STATE_NETWORK_ERROR:
                        loadmore.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        if (TDevice.hasInternet()) {
                            text.setText(R.string.loading_error);
                        } else {
                            text.setText(R.string.loading_no_network);
                        }
                        break;
                    default:
                        progress.setVisibility(View.GONE);
                        loadmore.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                        break;
                }
                return loadmore;
            }
        }
        return getRealView(position, convertView, parent);
    }

    protected View getRealView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
