package tk.wlemuel.cotable.base;

public abstract class BaseTabFragment extends BaseFragment {

    private TabChangedListener mListener;

    public BaseTabFragment() {
    }

    public final void a(TabChangedListener listener) {

        mListener = listener;
    }

    protected final boolean e() {
        return mListener.isCurrent(this);
    }


    public interface TabChangedListener {

        boolean isCurrent(BaseTabFragment fragment);
    }

    public void f() {
    }

    public void g() {
    }

    public void h() {
    }

    public void i() {
    }
}
