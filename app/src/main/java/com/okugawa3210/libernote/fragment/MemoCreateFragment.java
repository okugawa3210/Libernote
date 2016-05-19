package com.okugawa3210.libernote.fragment;

import android.content.DialogInterface;

import com.github.gfx.android.orma.SingleAssociation;
import com.okugawa3210.libernote.fragment.base.MemoFormFragment;
import com.okugawa3210.libernote.model.Memo;
import com.okugawa3210.libernote.model.MemoTag;
import com.okugawa3210.libernote.model.Tag;

import java.sql.Timestamp;
import java.util.List;

import rx.functions.Action0;
import rx.functions.Func1;

public class MemoCreateFragment extends MemoFormFragment {

    @Override
    protected DialogInterface.OnClickListener getSubmitCore() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Memo memo = new Memo();
                memo.setNote(form.getNote());
                memo.setTitle(form.getTitle());
                memo.setExpiredOn(form.getExpiredOn());
                memo.setPriority(form.getPriority());
                memo.setDone(false);
                memo.setUpdateOn(new Timestamp(System.currentTimeMillis()));

                final List<Tag> tags = form.getTags();

                orma.transactionAsync(new Runnable() {
                    @Override
                    public void run() {
                        Memo newMemo = Memo.create(orma, memo);
                        for (Tag tag : tags) {
                            MemoTag memoTag = new MemoTag();
                            memoTag.setMemo(new SingleAssociation<>(newMemo.getId(), newMemo));
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
                        return null;
                    }
                }).subscribe();
            }
        };
    }
}
