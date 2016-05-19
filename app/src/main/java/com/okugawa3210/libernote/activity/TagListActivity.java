package com.okugawa3210.libernote.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.fragment.TagAddDialogFragment;
import com.okugawa3210.libernote.fragment.TagListFragment;

public class TagListActivity extends AppCompatActivity implements TagAddDialogFragment.OnAddTagListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        assert toolbar != null && actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.menu_tag);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new TagListFragment()).commit();
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

    @Override
    public void onAddTagListener() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TagListFragment) {
                ((TagListFragment) fragment).update();
            }
        }
    }
}
