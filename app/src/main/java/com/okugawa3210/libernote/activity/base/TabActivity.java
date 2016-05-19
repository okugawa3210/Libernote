package com.okugawa3210.libernote.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.okugawa3210.libernote.R;

public abstract class TabActivity extends AppCompatActivity {

    private TabLayout tabLayout = null;
    private ViewPager viewPager = null;

    protected int getLayoutResID() {
        return R.layout.activity_tab_layout;
    }

    protected abstract PagerAdapter getPagerAdapter();

    protected TabLayout getTabLayout() {
        if (tabLayout == null) {
            tabLayout = (TabLayout) findViewById(R.id.tab_navigation);
        }
        return tabLayout;
    }

    protected ViewPager getViewPager() {
        if (viewPager == null) {
            viewPager = (ViewPager) findViewById(R.id.tab_pager);
        }
        return viewPager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        getViewPager().setAdapter(getPagerAdapter());
        getTabLayout().setupWithViewPager(getViewPager());
    }
}
