package com.okugawa3210.libernote.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.fragment.MemoCreateFragment;
import com.okugawa3210.libernote.fragment.MemoEditFragment;
import com.okugawa3210.libernote.fragment.TagSelectDialogFragment;
import com.okugawa3210.libernote.fragment.base.MemoFormFragment;
import com.okugawa3210.libernote.model.Tag;

import java.util.List;

public class MemoFormActivity extends AppCompatActivity implements TagSelectDialogFragment.OnSelectTagsListener {

    public static final String EXTRA_KEY_MEMO_ID = "memo_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        assert toolbar != null && actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);

        long memoId = getIntent().getLongExtra(EXTRA_KEY_MEMO_ID, 0);
        if (memoId > 0) {
            MemoEditFragment fragment = new MemoEditFragment();
            Bundle arg = new Bundle();
            arg.putLong(MemoEditFragment.ARG_KEY_MEMO_ID, memoId);
            fragment.setArguments(arg);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new MemoCreateFragment()).commit();
        }
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
    public void onSelectTagsListener(List<Tag> selectedTagList) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MemoFormFragment) {
                ((MemoFormFragment) fragment).renderTags(selectedTagList);
            }
        }
    }
}
