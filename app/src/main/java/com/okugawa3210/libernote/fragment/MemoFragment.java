package com.okugawa3210.libernote.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.okugawa3210.libernote.LibernoteApplication;
import com.okugawa3210.libernote.R;
import com.okugawa3210.libernote.activity.MemoFormActivity;
import com.okugawa3210.libernote.common.Constants;
import com.okugawa3210.libernote.model.Memo;
import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.model.Tag;
import com.okugawa3210.libernote.view.ConfirmDialog;
import com.okugawa3210.libernote.view.FlowLayout;
import com.okugawa3210.libernote.view.TagItem;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import rx.functions.Action0;
import rx.functions.Func1;

public class MemoFragment extends Fragment {

    public static final String ARG_KEY_MEMO_ID = "memo_id";

    @Inject
    OrmaDatabase orma;

    private boolean isInitialize = true;
    private Memo memo;
    private boolean isTrashed = false;

    private Toolbar toolbar;
    private TextView note;
    private TextView expiredOn;
    private TextView priority;
    private FlowLayout tagArea;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_memo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        LibernoteApplication.getComponent(this).inject(this);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        note = (TextView) view.findViewById(R.id.memo_note);
        expiredOn = (TextView) view.findViewById(R.id.memo_expired_on);
        priority = (TextView) view.findViewById(R.id.memo_priority);
        tagArea = (FlowLayout) view.findViewById(R.id.tag_area);

        renderMemo();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_memo, menu);
        if (isTrashed) {
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_trash).setVisible(false);
        } else {
            menu.findItem(R.id.menu_restore).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_restore:
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
                                getActivity().finish();
                            }
                        }).onErrorComplete(new Func1<Throwable, Boolean>() {
                            @Override
                            public Boolean call(Throwable throwable) {
                                Log.e(MemoFragment.class.getSimpleName(), throwable.getMessage());
                                return null;
                            }
                        }).subscribe();
                    }
                });
                break;
            case R.id.menu_edit:
                intent = new Intent(getActivity(), MemoFormActivity.class);
                intent.putExtra(MemoFormActivity.EXTRA_KEY_MEMO_ID, memo.getId());
                getActivity().startActivity(intent);
                return true;
            case R.id.menu_trash:
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
                                getActivity().finish();
                            }
                        }).onErrorComplete(new Func1<Throwable, Boolean>() {
                            @Override
                            public Boolean call(Throwable throwable) {
                                Log.e(MemoFragment.class.getSimpleName(), throwable.getMessage());
                                return null;
                            }
                        }).subscribe();
                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void update() {
        renderMemo();
    }

    private void renderMemo() {

        memo = Memo.getMemo(orma, getArguments().getLong(ARG_KEY_MEMO_ID));
        if (memo == null) {
            Toast.makeText(getContext(), getString(R.string.message_not_exist_memo), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        isTrashed = memo.isDone();

        toolbar.setTitle(memo.getTitle());

        note.setText(memo.getNote());

        if (memo.getExpiredOn() != null) {
            expiredOn.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(memo.getExpiredOn()));
        } else {
            expiredOn.setText(R.string.message_empty_text);
        }

        switch (memo.getPriority()) {
            case Constants.MEMO_PRIORITY_HIGH:
                priority.setText(getString(R.string.label_memo_priority_high));
                break;
            case Constants.MEMO_PRIORITY_LOWER:
                priority.setText(getString(R.string.label_memo_priority_lower));
                break;
            default:
                priority.setText(getString(R.string.label_memo_priority_none));
                break;
        }

        tagArea.removeAllViews();
        if (memo.getTags().size() > 0) {
            for (Tag tag : memo.getTags()) {
                TagItem item = new TagItem(getContext());
                item.setText(tag.getName());
                tagArea.addView(item);
            }
        } else {
            TextView textView = new TextView(getContext());
            textView.setText(R.string.message_empty_text);
            tagArea.addView(textView);
        }
    }
}
