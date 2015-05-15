package tk.wlemuel.cotable.activity.blog.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.Iterator;
import java.util.List;

import tk.wlemuel.cotable.activity.blog.BlogTab;
import tk.wlemuel.cotable.activity.blog.fragment.BlogListFragment;
import tk.wlemuel.cotable.base.BaseTabFragment;
import tk.wlemuel.cotable.ui.pagertab.SlidingTabPagerAdapter;

/**
 * BlogTabPagerAdapter
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogTabPagerAdapter
 * @created 2015/05/10
 * @updated 2015/05/10
 */
public final class BlogTabPagerAdapter extends SlidingTabPagerAdapter {
    public BlogTabPagerAdapter(FragmentManager mgr, Context context,
                               ViewPager viewPager) {
        super(mgr, BlogTab.values().length, context.getApplicationContext(),
                viewPager);

        BlogTab[] tabs = BlogTab.values();

        for (BlogTab tab : tabs) {
            Fragment fragment = null;
            List<Fragment> list = mgr.getFragments();
            if (list != null) {
                Iterator<Fragment> iter = list.iterator();
                while (iter.hasNext()) {
                    fragment = iter.next();
                    if (fragment.getClass() == tab.getClz())
                        break;
                }
            }

            BaseTabFragment tabFragment = (BaseTabFragment) fragment;
            if (tabFragment == null) {
                try {
                    tabFragment = (BaseTabFragment) tab.getClz().newInstance();
                    tabFragment.setCatalog(tab.getCatalog());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            tabFragment.addListener(this);
            if (!tabFragment.isAdded()) {
                Bundle args = new Bundle();
                args.putInt(BlogListFragment.BUNDLE_KEY_CATALOG,
                        tab.getCatalog());
                tabFragment.setArguments(args);
            }
            fragments[tab.getIdx()] = tabFragment;
        }
    }

    public final int getCacheCount() {
        return 2;
    }

    public final int getCount() {
        return BlogTab.values().length;
    }

    public final CharSequence getPageTitle(int i) {
        BlogTab tab = BlogTab.getTabByIdx(i);
        int idx = 0;
        CharSequence title = "";
        if (tab != null)
            idx = tab.getTitle();
        if (idx != 0)
            title = context.getText(idx);
        return title;
    }

}
