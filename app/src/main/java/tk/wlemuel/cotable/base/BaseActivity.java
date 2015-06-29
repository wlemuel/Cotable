package tk.wlemuel.cotable.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.utils.TDevice;

/**
 * BaseActivity
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BaseActivity
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected LayoutInflater mInflater;
    protected Toolbar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasActionBar()) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        onBeforeSetContentLayout();

        if (getLayoutRes() != 0)
            setContentView(getLayoutRes());

        mActionBar = (Toolbar) findViewById(R.id.actionBar);
        mInflater = getLayoutInflater();

        if (hasActionBar())
            initActionBar(mActionBar);

        init(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onBeforeSetContentLayout() {

    }

    protected boolean hasActionBar() {
        return true;
    }

    protected int getLayoutRes() {
        return 0;
    }

    protected View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    protected int getActionBarTitle() {
        return R.string.app_name;
    }

    public void setActionBarTitle(String title) {
        if (hasActionBar()) {
            if (mActionBar != null)
                mActionBar.setTitle(title);
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected int getActionBarCustomView() {
        return 0;
    }

    protected void init(Bundle savedInstanceState) {

    }

    protected void initActionBar(Toolbar actionBar) {
        if (actionBar == null) return;

        setSupportActionBar(actionBar);

        if (hasBackButton()) {
            int layoutRes = getActionBarCustomView();
            if (layoutRes != 0) {
                View view = inflateView(layoutRes);
                Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams
                        .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            int titleRes = getActionBarTitle();

            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
            }

            actionBar.setNavigationIcon(R.drawable.actionbar_back_icon_normal);
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            actionBar.setPadding(0, 0, 0, 0);
        } else {
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
            }

            actionBar.setPadding((int) TDevice.dpToPixel(16), 0, 0, 0);

        }
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0)
            setActionBarTitle(getString(resId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
