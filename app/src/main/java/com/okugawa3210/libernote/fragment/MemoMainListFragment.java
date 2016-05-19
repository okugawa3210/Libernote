package com.okugawa3210.libernote.fragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.SubMenu;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.adapter.MemoListAdapter;
import com.okugawa3210.libernote.fragment.base.MemoListFragment;
import com.okugawa3210.libernote.model.Memo;

public class MemoMainListFragment extends MemoListFragment {

    @Override
    protected void renderMemoList() {
        getListView().setAdapter(null);
        setAdapter(new MemoListAdapter(getActivity()));
        getAdapter().setList(Memo.getMemos(orma, getCurrentMemoSortColumn(), isCurrentSortReverse()));
        getListView().setAdapter(getAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SubMenu sueMenuSort = menu.findItem(R.id.menu_sort).getSubMenu();
        sueMenuSort.clear();
        inflater.inflate(R.menu.menu_sort_submenu, sueMenuSort);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
