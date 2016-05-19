package com.okugawa3210.libernote.fragment.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.activity.MemoActivity;
import com.okugawa3210.libernote.activity.MemoFormActivity;
import com.okugawa3210.libernote.activity.MemoSearchActivity;
import com.okugawa3210.libernote.adapter.MemoListAdapter;
import com.okugawa3210.libernote.common.MemoSortColumn;
import com.okugawa3210.libernote.model.Memo;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.view.ConfirmDialog;

import javax.inject.Inject;

import rx.functions.Action0;
import rx.functions.Func1;

public abstract class MemoListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private boolean isInitialize = true;
    private ListView listView;
    private MemoListAdapter adapter;
    private MemoSortColumn currentSortColumn = MemoSortColumn.UPDATE_ON;
    private boolean currentSortReverse = false;

    @Inject
    protected OrmaDatabase orma;

    protected ListView getListView() {
        return listView;
    }

    protected MemoListAdapter getAdapter() {
        return adapter;
    }

    protected void setAdapter(MemoListAdapter adapter) {
        this.adapter = adapter;
    }

    protected MemoSortColumn getCurrentMemoSortColumn() {
        return currentSortColumn;
    }

    protected boolean isCurrentSortReverse() {
        return currentSortReverse;
    }

    protected abstract void renderMemoList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        LibernoteApplication.getComponent(this).inject(this);

        listView = (ListView) view.findViewById(R.id.list);
        assert listView != null;

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setEmptyView(view.findViewById(R.id.list_empty));
        listView.setCacheColorHint(ContextCompat.getColor(getContext(), android.R.color.transparent));
        listView.setScrollingCacheEnabled(false);

        renderMemoList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_search:
                intent = new Intent(getActivity(), MemoSearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_sort:
                MenuItem currentSortSubmenu = null;
                switch (currentSortColumn) {
                    case UPDATE_ON:
                        currentSortSubmenu = item.getSubMenu().findItem(R.id.submenu_sort_update_on);
                        break;
                    case PRIORITY:
                        currentSortSubmenu = item.getSubMenu().findItem(R.id.submenu_sort_priority);
                        break;
                    case EXPIRED_ON:
                        currentSortSubmenu = item.getSubMenu().findItem(R.id.submenu_sort_expired_on);
                        break;
                }
                if (currentSortSubmenu != null) {
                    for (int i = 0; i < item.getSubMenu().size(); i++) {
                        item.getSubMenu().getItem(i).setIcon(null);
                    }
                    currentSortSubmenu.setIcon(currentSortReverse ? R.drawable.ic_sort_reverse : R.drawable.ic_sort_forword);
                }
                break;
            case R.id.submenu_sort_update_on:
                if (currentSortColumn == MemoSortColumn.UPDATE_ON) {
                    currentSortReverse = !currentSortReverse;
                } else {
                    currentSortColumn = MemoSortColumn.UPDATE_ON;
                    currentSortReverse = false;
                }
                update();
                break;
            case R.id.submenu_sort_priority:
                if (currentSortColumn == MemoSortColumn.PRIORITY) {
                    currentSortReverse = !currentSortReverse;
                } else {
                    currentSortColumn = MemoSortColumn.PRIORITY;
                    currentSortReverse = false;
                }
                update();
                break;
            case R.id.submenu_sort_expired_on:
                if (currentSortColumn == MemoSortColumn.EXPIRED_ON) {
                    currentSortReverse = !currentSortReverse;
                } else {
                    currentSortColumn = MemoSortColumn.EXPIRED_ON;
                    currentSortReverse = false;
                }
                update();
                break;
            case R.id.menu_create:
                intent = new Intent(getActivity(), MemoFormActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isInitialize) {
            update();
        }
        isInitialize = false;
    }

    @Override
    public void onDestroyView() {
        getListView().setAdapter(null);
        super.onDestroyView();
    }

    public void update() {
        renderMemoList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Memo memo = getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), MemoActivity.class);
        intent.putExtra(MemoActivity.EXTRA_KEY_MEMO_ID, memo.getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Memo memo = getAdapter().getItem(position);
        ConfirmDialog.show(getActivity(), getString(R.string.message_confirm_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orma.transactionAsync(new Runnable() {
                    @Override
                    public void run() {
                        Memo.updateDone(orma, memo.getId(), true);
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
