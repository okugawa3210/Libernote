package com.okugawa3210.libernote.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.adapter.base.ListAdapter;
import com.okugawa3210.libernote.model.Tag;

public class TagListAdapter extends ListAdapter<Tag> {
    public TagListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final TagViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_tag, parent, false);
            holder = new TagViewHolder((ListView) parent);
            holder.name = (TextView) view.findViewById(R.id.list_item_name);

            view.setTag(holder);
        } else {
            holder = (TagViewHolder) view.getTag();
        }

        Tag tag = getItem(position);

        holder.name.setText(tag.getName());

        return view;
    }

    public class TagViewHolder {
        private ListView parent;
        private TextView name;

        public TagViewHolder(ListView parent) {
            this.parent = parent;
        }
    }
}
