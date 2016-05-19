package com.okugawa3210.libernote.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.fragment.MemoFragment;

public class MemoActivity extends AppCompatActivity {

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

        Bundle args = new Bundle();
        args.putLong(MemoFragment.ARG_KEY_MEMO_ID, getIntent().getLongExtra(EXTRA_KEY_MEMO_ID, 0L));
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
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
}
