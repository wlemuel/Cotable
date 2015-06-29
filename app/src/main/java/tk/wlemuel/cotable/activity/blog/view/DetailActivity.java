package tk.wlemuel.cotable.activity.blog.view;

import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import tk.wlemuel.cotable.activity.blog.Interface.AutoHideToolBarListener;
import tk.wlemuel.cotable.activity.blog.fragment.BlogDetailFragment;
import tk.wlemuel.cotable.activity.common.SimpleBackActivity;
import tk.wlemuel.cotable.base.BaseFragment;
import tk.wlemuel.cotable.utils.BlogReaderAnim;

/**
 * DetailActivity
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc DetailActivity
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class DetailActivity extends SimpleBackActivity implements AutoHideToolBarListener {

    public static final int DISPLAY_BLOG = 0;
    public static final int DIESPLAY_NEWS = 1;
    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";

    private WeakReference<BaseFragment> mFragment;


    @Override
    public void onShowHideToolbar(boolean show) {
        if (!isFinishing()) {
            BlogReaderAnim.animateTopBar(mActionBar, show);
        }
    }

    @Override
    protected  void onAfterInitDefaultFragmentLayout(Fragment fragment){
            if(fragment instanceof BlogDetailFragment){
                BlogDetailFragment blogDetailFragment = (BlogDetailFragment)fragment;
                blogDetailFragment.setOnAutoHideToolBarListener(this);
            }
    }
}
