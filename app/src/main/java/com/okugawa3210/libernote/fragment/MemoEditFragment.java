package com.okugawa3210.libernote.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.gfx.android.orma.SingleAssociation;
import com.okugawa3210.libernote.fragment.base.MemoFormFragment;
import com.okugawa3210.libernote.model.Memo;
import com.okugawa3210.libernote.model.MemoTag;
import com.okugawa3210.libernote.model.Tag;

import java.sql.Timestamp;
import java.util.List;

import rx.functions.Action0;
import rx.functions.Func1;

public class MemoEditFragment extends MemoFormFragment {

    public static final String ARG_KEY_MEMO_ID = "memo_id";

    private Memo memo;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        memo = Memo.getMemo(orma, getArguments().getLong(ARG_KEY_MEMO_ID));

        if (memo == null) {
            Toast.makeText(getContext(), "メモが存在しません", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        renderMemo();
    }

    @Override
    protected DialogInterface.OnClickListener getSubmitCore() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Memo cond = new Memo();
                cond.setId(memo.getId());
                cond.setNote(form.getNote());
                cond.setTitle(form.getTitle());
                cond.setExpiredOn(form.getExpiredOn());
                cond.setPriority(form.getPriority());
                cond.setDone(false);
                cond.setUpdateOn(new Timestamp(System.currentTimeMillis()));

                final List<Tag> tags = form.getTags();

                orma.transactionAsync(new Runnable() {
                    @Override
                    public void run() {
                        Memo.update(orma, cond);
                        MemoTag.deleteByMemoId(orma, cond);

                        for (Tag tag : tags) {
                            MemoTag memoTag = new MemoTag();
                            memoTag.setMemo(new SingleAssociation<>(cond.getId(), cond));
                            memoTag.setTag(new SingleAssociation<>(tag.getId(), tag));

                            MemoTag.create(orma, memoTag);
                        }
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        getActivity().finish();
                    }
                }).onErrorComplete(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(MemoEditFragment.class.getSimpleName(), throwable.getMessage());
                        return null;
                    }
                }).subscribe();
            }
        };
    }

    private void renderMemo() {
        form.setTitle(memo.getTitle());
        form.setNote(memo.getNote());
        form.setExpiredOn(memo.getExpiredOn());
        form.setPriority(memo.getPriority());
        form.setTag(memo.getTags());
    }
}
