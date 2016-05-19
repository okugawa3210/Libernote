package com.okugawa3210.libernote.fragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.adapter.MemoListAdapter;
import com.okugawa3210.libernote.common.SearchMode;
import com.okugawa3210.libernote.fragment.base.MemoListFragment;
import com.okugawa3210.libernote.model.Memo;
import com.okugawa3210.libernote.view.ConfirmDialog;

import rx.functions.Action0;
import rx.functions.Func1;

public class MemoTrashListFragment extends MemoListFragment {

    @Override
    protected void renderMemoList() {
        getListView().setAdapter(null);
        setAdapter(new MemoListAdapter(getActivity()));
        getAdapter().setList(Memo.getMemos(orma, null, SearchMode.TRASH, getCurrentMemoSortColumn(), isCurrentSortReverse()));
        getListView().setAdapter(getAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_memo_trash, menu);
        SubMenu sueMenuSort = menu.findItem(R.id.menu_sort).getSubMenu();
        sueMenuSort.clear();
        inflater.inflate(R.menu.menu_sort_submenu, sueMenuSort);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_empty_trash:
                ConfirmDialog.show(getActivity(), getString(R.string.message_confirm_empty_trash), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orma.transactionAsync(new Runnable() {
                            @Override
                            public void run() {
                                Memo.deleteCompletely(orma);
                            }
                        }).doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                update();
                            }
                        }).onErrorComplete(new Func1<Throwable, Boolean>() {
                            @Override
                            public Boolean call(Throwable throwable) {
                                Log.e(MemoListFragment.class.getSimpleName(), throwable.getMessage());
                                return null;
                            }
                        }).subscribe();
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Memo memo = getAdapter().getItem(position);
        ConfirmDialog.show(getActivity(), getString(R.string.message_confirm_restore), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orma.transactionAsync(new Runnable() {
                    @Override
                    public void run() {
                        Memo.updateDone(orma, memo.getId(), false);
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        update();
                    }
                }).onErrorComplete(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        Log.e(MemoListFragment.class.getSimpleName(), throwable.getMessage());
                        return null;
                    }
                }).subscribe();
            }
        });
        return true;
    }
}
