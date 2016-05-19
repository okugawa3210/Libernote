package com.okugawa3210.libernote.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.okugawa3210.libernote.R;

public class TagItem extends Button {

    public TagItem(Context context) {
        this(context, null, false);
    }

    public TagItem(Context context, final ViewGroup parent, boolean closeable) {
        super(context);

        setMinWidth(1);
        setMinHeight(1);
        setMinimumWidth(1);
        setMinimumHeight(1);

        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int margin = getDimensionPixelSize(R.dimen.tag_item_margin);
        params.setMargins(margin, margin, margin, margin);
        setLayoutParams(params);

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(getColor(R.color.tag_item_background));
        shape.setCornerRadius(getDimensionPixelSize(R.dimen.tag_item_corner));
        setBackground(shape);

        setTextColor(getColor(R.color.tag_item_foreground));
        setTextSize(getDimensionPixelSize(R.dimen.tag_item_text_size));

        setGravity(Gravity.CENTER);

        int padding = getDimensionPixelSize(R.dimen.tag_item_padding);

        if (closeable) {
            setPadding(padding, padding, padding * 2, padding);
            setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_remove_white), null, null, null);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.removeView(v);
                }
            });
        } else {
            setPadding(padding, padding / 2, padding, padding / 2);
            setClickable(false);
            setFocusable(false);
        }
    }

    private int getDimensionPixelSize(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }
}
