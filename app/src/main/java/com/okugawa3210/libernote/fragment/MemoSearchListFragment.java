package com.okugawa3210.libernote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.activity.MemoSearchActivity;
import com.okugawa3210.libernote.adapter.MemoListAdapter;
import com.okugawa3210.libernote.common.SearchMode;
import com.okugawa3210.libernote.fragment.base.MemoListFragment;
import com.okugawa3210.libernote.model.Memo;

public class MemoSearchListFragment extends MemoListFragment {

    public static final String ARG_KEY_SEARCH_MODE = "search_mode";

    private SearchMode searchMode;
    private String searchWord;

    protected void renderMemoList() {
        getListView().setAdapter(null);
        setAdapter(new MemoListAdapter(getActivity()));
        getAdapter().setList(Memo.getMemos(orma, searchWord, searchMode, getCurrentMemoSortColumn(), isCurrentSortReverse()));
        getListView().setAdapter(getAdapter());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.searchMode = (SearchMode) getArguments().getSerializable(ARG_KEY_SEARCH_MODE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                update();
                LibernoteApplication.hideSoftInputFromWindow();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static MemoSearchListFragment newInstance(SearchMode mode) {
        MemoSearchListFragment fragment = new MemoSearchListFragment();
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_KEY_SEARCH_MODE, mode);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void update() {

        if (getActivity() instanceof MemoSearchActivity) {
            this.searchWord = ((MemoSearchActivity) getActivity()).getSearchWord();
        }

        renderMemoList();
    }
}
