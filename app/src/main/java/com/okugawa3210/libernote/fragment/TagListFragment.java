package com.okugawa3210.libernote.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.activity.MemoSearchActivity;
import com.okugawa3210.libernote.adapter.TagListAdapter;
import com.okugawa3210.libernote.common.SearchMode;
import com.okugawa3210.libernote.model.MemoTag;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.model.Tag;
import com.okugawa3210.libernote.view.ConfirmDialog;

import javax.inject.Inject;

import rx.functions.Action0;
import rx.functions.Func1;

public class TagListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private boolean isInitialize = true;
    private ListView listView;
    private TagListAdapter adapter;

    @Inject
    protected OrmaDatabase orma;

    protected ListView getListView() {
        return listView;
    }

    protected TagListAdapter getAdapter() {
        return adapter;
    }

    protected void setAdapter(TagListAdapter adapter) {
        this.adapter = adapter;
    }

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

        renderTagList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tag_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                new TagAddDialogFragment().show(getActivity().getSupportFragmentManager(), TagAddDialogFragment.class.getSimpleName());
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
        renderTagList();
    }

    protected void renderTagList() {
        getListView().setAdapter(null);
        setAdapter(new TagListAdapter(getActivity()));
        getAdapter().setList(Tag.getTags(orma));
        getListView().setAdapter(getAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), MemoSearchActivity.class);
        intent.putExtra(MemoSearchActivity.EXTRA_SEARCH_MODE, SearchMode.TAG);
        intent.putExtra(MemoSearchActivity.EXTRA_SEARCH_WORD, getAdapter().getItem(position).getName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Tag tag = getAdapter().getItem(position);
        ConfirmDialog.show(getActivity(), getString(R.string.message_confirm_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orma.transactionAsync(new Runnable() {
                    @Override
                    public void run() {
                        MemoTag.deleteByTagId(orma, tag);
                        Tag.delete(orma, tag.getId());
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        update();
                    }
                }).onErrorComplete(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        Log.e(TagListFragment.class.getSimpleName(), throwable.getMessage());
                        return null;
                    }
                }).subscribe();
            }
        });
        return true;
    }
}
