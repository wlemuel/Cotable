package tk.wlemuel.cotable.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.lang.ref.WeakReference;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.base.BaseActivity;
import tk.wlemuel.cotable.base.BaseFragment;
import tk.wlemuel.cotable.model.SimpleBackPage;

/**
 * SimpleBackActivity
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc SimpleBackActivity
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class SimpleBackActivity extends BaseActivity {

    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    private static final String TAG = SimpleBackActivity.class.getSimpleName();
    private WeakReference<Fragment> mFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.aty_simple_fragment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        Intent data = getIntent();

        if (data == null) {
            throw new RuntimeException("You must specify a page info to display.");
        }

        int pageValue = data.getIntExtra(BUNDLE_KEY_PAGE, 0);
        SimpleBackPage page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("Can not find page by value: " + pageValue);
        }

        setActionBarTitle(page.getTitle());

        try {
            Fragment fragment = (Fragment) page.getClz().newInstance();

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            if (args != null) {
                fragment.setArguments(args);
            }


            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, fragment, TAG);
            trans.commit();

            mFragment = new WeakReference<Fragment>(fragment);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.get() != null
                && mFragment.get() instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) mFragment.get();
            if (!bf.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
