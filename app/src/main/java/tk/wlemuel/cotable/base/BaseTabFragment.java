package tk.wlemuel.cotable.base;

import tk.wlemuel.cotable.model.BlogList;

public abstract class BaseTabFragment extends BaseFragment {

    protected int mCatalog = BlogList.CATALOG_LASTEST;
    private TabChangedListener mListener;

    public BaseTabFragment() {
    }

    public void setCatalog(int catalog) {
        mCatalog = catalog;
    }

    public final void addListener(TabChangedListener listener) {
        mListener = listener;
    }

    protected final boolean e() {
        return mListener.isCurrent(this);
    }

    public void f() {
    }

    public void g() {
    }

    public void h() {
    }

    public void i() {
    }

    public interface TabChangedListener {

        boolean isCurrent(BaseTabFragment fragment);
    }
}
