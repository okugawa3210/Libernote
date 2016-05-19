package com.okugawa3210.libernote.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.okugawa3210.libernote.model.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<T extends BaseModel> extends BaseAdapter {

    private Context context;
    protected LayoutInflater layoutInflater;
    protected List<T> list;

    public ListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = new ArrayList<>();
    }

    /**
     * Get Context
     *
     * @return Context
     */
    protected Context getContext() {
        return this.context;
    }

    /**
     * Set List
     *
     * @param list List
     */
    public void setList(List<T> list) {
        this.list.clear();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
