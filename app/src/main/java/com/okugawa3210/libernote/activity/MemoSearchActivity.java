package com.okugawa3210.libernote.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.activity.base.TabActivity;
import com.okugawa3210.libernote.adapter.MemoSearchPagerAdapter;
import com.okugawa3210.libernote.common.SearchMode;
import com.okugawa3210.libernote.view.MenuMemoSearch;

public class MemoSearchActivity extends TabActivity {

    public static final String EXTRA_SEARCH_MODE = "extra_search_mode";
    public static final String EXTRA_SEARCH_WORD = "extra_search_word";

    private ActionBar actionBar;
    private MemoSearchPagerAdapter adapter = null;

    @Override
    protected MemoSearchPagerAdapter getPagerAdapter() {
        if (adapter == null) {
            adapter = new MemoSearchPagerAdapter(getSupportFragmentManager());
        }
        return adapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        assert toolbar != null && actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);

        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(new MenuMemoSearch(this), layoutParams);

        actionBar.getCustomView().findViewById(R.id.search_word).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (KeyEvent.KEYCODE_ENTER == keyCode) {
                        getPagerAdapter().findCurrentFragment(getViewPager()).update();
                    }
                }
                return false;
            }
        });

        getTabLayout().setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getViewPager().setCurrentItem(tab.getPosition());
                getPagerAdapter().findCurrentFragment(getViewPager()).update();
                LibernoteApplication.hideSoftInputFromWindow();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                getPagerAdapter().findCurrentFragment(getViewPager()).update();
                LibernoteApplication.hideSoftInputFromWindow();
            }
        });

        getTabLayout().post(new Runnable() {
            @Override
            public void run() {
                EditText editText = ((MenuMemoSearch) actionBar.getCustomView()).getSearchWord();
                editText.setText(getIntent().getStringExtra(EXTRA_SEARCH_WORD));
                editText.clearFocus();
                SearchMode searchMode = (SearchMode) getIntent().getSerializableExtra(EXTRA_SEARCH_MODE);
                if (searchMode != null) {
                    TabLayout.Tab selectedTab = null;
                    switch (searchMode) {
                        case NOTE:
                            selectedTab = getTabLayout().getTabAt(0);
                            break;
                        case TAG:
                            selectedTab = getTabLayout().getTabAt(1);
                            break;
                        default:
                            break;
                    }
                    if (selectedTab != null) {
                        selectedTab.select();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo_search, menu);
        SubMenu sueMenuSort = menu.findItem(R.id.menu_sort).getSubMenu();
        sueMenuSort.clear();
        getMenuInflater().inflate(R.menu.menu_sort_submenu, sueMenuSort);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getSearchWord() {
        return ((MenuMemoSearch) actionBar.getCustomView()).getSearchWord().getText().toString();
    }
}
