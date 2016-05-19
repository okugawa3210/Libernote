package com.okugawa3210.libernote.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.common.SearchMode;
import com.okugawa3210.libernote.fragment.MemoSearchListFragment;

public class MemoSearchPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    final SearchMode[] SEARCH_MODE = new SearchMode[]{SearchMode.NOTE, SearchMode.TAG};

    public MemoSearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MemoSearchListFragment.newInstance(SEARCH_MODE[position]);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return LibernoteApplication.getContext().getString(R.string.tab_title_text_search);
            case 1:
                return LibernoteApplication.getContext().getString(R.string.tab_title_tag_search);
            default:
                return null;
        }
    }

    public MemoSearchListFragment findCurrentFragment(ViewPager viewPager) {
        return (MemoSearchListFragment) instantiateItem(viewPager, viewPager.getCurrentItem());
    }
}
