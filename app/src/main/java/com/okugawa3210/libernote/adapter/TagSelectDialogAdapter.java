package com.okugawa3210.libernote.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.adapter.base.ListAdapter;
import com.okugawa3210.libernote.model.Tag;

public class TagSelectDialogAdapter extends ListAdapter<Tag> {

    public TagSelectDialogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final TagViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.select_item_tag, parent, false);
            holder = new TagViewHolder((ListView) parent);
            holder.name = (CheckBox) view.findViewById(R.id.list_item_name);
            holder.setOnCheckedListener(position);

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
        private CheckBox name;

        public TagViewHolder(ListView parent) {
            this.parent = parent;
        }

        public CheckBox getName() {
            return name;
        }

        private void setOnCheckedListener(final int position) {
            name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getItem(position).setSelected(isChecked);
                }
            });
        }

        public boolean isChecked() {
            return name.isChecked();
        }
    }
}
