package com.okugawa3210.libernote.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.okugawa3210.libernote.R;

public class MenuMemoSearch extends LinearLayout {

    private EditText searchWord;

    public MenuMemoSearch(Context context) {
        this(context, null);
    }

    public MenuMemoSearch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuMemoSearch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View layout = LayoutInflater.from(context).inflate(R.layout.menu_memo_search, this);
        searchWord = (EditText) layout.findViewById(R.id.search_word);
    }

    public EditText getSearchWord() {
        return searchWord;
    }
}
