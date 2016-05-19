package com.okugawa3210.libernote.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.adapter.base.ListAdapter;
import com.okugawa3210.libernote.common.Constants;
import com.okugawa3210.libernote.model.Memo;
import com.okugawa3210.libernote.model.Tag;
import com.okugawa3210.libernote.view.FlowLayout;
import com.okugawa3210.libernote.view.TagItem;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MemoListAdapter extends ListAdapter<Memo> {
    public MemoListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final MemoViewHolder holder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_memo, parent, false);
            holder = new MemoViewHolder((ListView) parent);
            holder.updateOn = (TextView) view.findViewById(R.id.list_item_update_on);
            holder.note = (TextView) view.findViewById(R.id.list_item_note);
            holder.tagArea = (FlowLayout) view.findViewById(R.id.list_item_tag_area);

            view.setTag(holder);
        } else {
            holder = (MemoViewHolder) view.getTag();
        }

        Memo memo = getItem(position);

        holder.updateOn.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(memo.getUpdateOn()));
        holder.note.setText(memo.getNote());

        switch (memo.getPriority()) {
            case Constants.MEMO_PRIORITY_HIGH:
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.priority_high));
                break;
            case Constants.MEMO_PRIORITY_LOWER:
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.priority_lower));
                break;
            default:
                view.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                break;
        }

        holder.tagArea.removeAllViews();
        for (Tag tag : memo.getTags()) {
            TagItem button = new TagItem(getContext());
            button.setText(tag.getName());
            holder.tagArea.addView(button);
        }

        return view;
    }

    public class MemoViewHolder {
        private ListView parent;
        private TextView updateOn;
        private TextView note;
        private FlowLayout tagArea;

        public MemoViewHolder(ListView parent) {
            this.parent = parent;
        }

        private AdapterView.OnItemClickListener getOnItemClickListener() {
            return parent.getOnItemClickListener();
        }
    }
}
