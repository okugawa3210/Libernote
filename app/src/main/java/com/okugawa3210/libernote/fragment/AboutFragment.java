package com.okugawa3210.libernote.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okugawa3210.libernote.R;

import java.util.HashMap;
import java.util.Map;

public class AboutFragment extends Fragment {

    private final int aboutItemCount = 3;
    private Map<Integer, AboutContentHolder> aboutItems = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aboutItems.put(R.id.about_item_app, new AboutContentHolder(view.findViewById(R.id.about_item_app), view.findViewById(R.id.about_content_app)));
        aboutItems.put(R.id.about_item_license, new AboutContentHolder(view.findViewById(R.id.about_item_license), view.findViewById(R.id.about_content_license)));
        aboutItems.put(R.id.about_item_contact, new AboutContentHolder(view.findViewById(R.id.about_item_contact), view.findViewById(R.id.about_content_contact)));

        aboutItems.get(R.id.about_item_app).item.callOnClick();

        try {
            ((TextView) view.findViewById(R.id.app_version)).setText(String.valueOf(getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(AboutFragment.class.getSimpleName(), e.getMessage());
        }
    }

    private View.OnClickListener OnClickItemListener = new View.OnClickListener() {

        int expandedId = -1;

        @Override
        public void onClick(View v) {

            AboutContentHolder holder = aboutItems.get(v.getId());
            if (holder.expanded) {
                holder.doCollapse();
                expandedId = -1;
            } else {
                if (aboutItems.containsKey(expandedId)) {
                    aboutItems.get(expandedId).doCollapse();
                }
                holder.doExpand();
                expandedId = v.getId();
            }
        }
    };

    private class AboutContentHolder {

        TextView item;
        LinearLayout content;
        boolean expanded = false;

        AboutContentHolder(View item, View content) {
            this.item = (TextView) item;
            this.content = (LinearLayout) content;
            this.content.setVisibility(View.GONE);
            this.item.setOnClickListener(OnClickItemListener);
        }

        void doExpand() {
            this.expanded = true;
            this.item.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_less), null, null, null);
            this.content.setVisibility(View.VISIBLE);
        }

        void doCollapse() {
            this.expanded = false;
            this.item.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_more), null, null, null);
            this.content.setVisibility(View.GONE);
        }
    }
}
