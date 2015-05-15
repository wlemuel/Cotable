package tk.wlemuel.cotable.activity.blog.view;

import java.lang.ref.WeakReference;

import tk.wlemuel.cotable.activity.common.SimpleBackActivity;
import tk.wlemuel.cotable.base.BaseFragment;

/**
 * DetailActivity
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc DetailActivity
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class DetailActivity extends SimpleBackActivity {

    public static final int DISPLAY_BLOG = 0;
    public static final int DIESPLAY_NEWS = 1;
    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";

    private WeakReference<BaseFragment> mFragment;


}
