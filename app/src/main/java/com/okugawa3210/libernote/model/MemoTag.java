package com.okugawa3210.libernote.model;

import android.support.annotation.NonNull;

import com.github.gfx.android.orma.ModelFactory;
import com.github.gfx.android.orma.SingleAssociation;
import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class MemoTag {
    @Column(value = "memo_id", indexed = true)
    SingleAssociation<Memo> memo;
    @Column(value = "tag_id", indexed = true)
    SingleAssociation<Tag> tag;

    public SingleAssociation<Memo> getMemo() {
        return memo;
    }

    public void setMemo(SingleAssociation<Memo> memo) {
        this.memo = memo;
    }

    public SingleAssociation<Tag> getTag() {
        return tag;
    }

    public void setTag(SingleAssociation<Tag> tag) {
        this.tag = tag;
    }

    public static MemoTag create(OrmaDatabase orma, final MemoTag memoTag) {
        return orma.createMemoTag(new ModelFactory<MemoTag>() {
            @NonNull
            @Override
            public MemoTag call() {
                return memoTag;
            }
        });
    }

    public static int deleteByMemoId(OrmaDatabase orma, Memo memo) {
        return orma.deleteFromMemoTag()
                .memoEq(memo)
                .execute();
    }

    public static int deleteByTagId(OrmaDatabase orma, Tag tag) {
        return orma.deleteFromMemoTag()
                .tagEq(tag)
                .execute();
    }
}
