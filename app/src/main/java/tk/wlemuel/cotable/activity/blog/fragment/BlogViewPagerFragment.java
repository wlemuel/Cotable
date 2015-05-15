package tk.wlemuel.cotable.activity.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.activity.blog.adapter.BlogTabPagerAdapter;
import tk.wlemuel.cotable.ui.pagertab.PagerSlidingTabStrip;

/**
 * BlogViewPagerFragment
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogViewPagerFragment
 * @created 2015/05/10
 * @updated 2015/05/10
 */
public class BlogViewPagerFragment extends Fragment implements
        OnPageChangeListener {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private BlogTabPagerAdapter mTabAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container,
                false);
        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);

        if (mViewPager.getAdapter() == null &&
                mTabAdapter == null) {
            mTabAdapter = new BlogTabPagerAdapter(getChildFragmentManager(),
                    getActivity(), mViewPager);
        }
        mViewPager.setOffscreenPageLimit(mTabAdapter.getCacheCount());
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOnPageChangeListener(this);
        mTabStrip.setViewPager(mViewPager);

        return view;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        mTabStrip.onPageScrollStateChanged(arg0);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        mTabStrip.onPageScrolled(arg0, arg1, arg2);
        mTabAdapter.onPageScrolled(arg0);
    }

    @Override
    public void onPageSelected(int arg0) {
        mTabStrip.onPageSelected(arg0);
        mTabAdapter.onPageSelected(arg0);
    }
}
