package com.okugawa3210.libernote.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.github.gfx.android.orma.ModelFactory;
import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import com.okugawa3210.libernote.common.MemoSortColumn;
import com.okugawa3210.libernote.common.SearchMode;
import com.okugawa3210.libernote.model.base.BaseModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table
public class Memo implements BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;
    @Column(indexed = true)
    @Nullable
    String title;
    @Column(indexed = true)
    @Nullable
    String note;
    @Column(value = "expired_on", indexed = true)
    @Nullable
    Date expiredOn;
    @Column(value = "update_on", indexed = true)
    Timestamp updateOn;
    @Column(indexed = true)
    boolean done;
    @Column(indexed = true)
    int priority;

    List<Tag> tags = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    @Nullable
    public Date getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(@Nullable Date expiredOn) {
        this.expiredOn = expiredOn;
    }

    public Timestamp getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Timestamp updateOn) {
        this.updateOn = updateOn;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTags(OrmaDatabase orma) {
        for (MemoTag memoTag : orma.selectFromMemoTag().memoEq(this).toList()) {
            tags.add(memoTag.getTag().value());
        }
    }

    public List<Tag> getTags() {
        return tags;
    }

    public static List<Memo> getMemos(OrmaDatabase orma, MemoSortColumn sortColumn, boolean isReverse) {
        return getMemos(orma, null, SearchMode.NONE, sortColumn, isReverse);
    }

    public static List<Memo> getMemos(OrmaDatabase orma, String searchWord, SearchMode searchMode, MemoSortColumn sortColumn, boolean isReverse) {
        List<Memo> memos = new ArrayList<>();

        Memo_Selector selector = orma.selectFromMemo();

        switch (sortColumn) {
            case UPDATE_ON:
                if (isReverse) {
                    selector.orderByUpdateOnAsc();
                } else {
                    selector.orderByUpdateOnDesc();
                }
                break;
            case PRIORITY:
                if (isReverse) {
                    selector.orderByPriorityAsc().orderByUpdateOnAsc();
                } else {
                    selector.orderByPriorityDesc().orderByUpdateOnDesc();
                }
                break;
            case EXPIRED_ON:
                if (isReverse) {
                    selector.orderBy(Memo_Schema.expiredOn.getEscapedName() + " IS NULL DESC").orderByExpiredOnDesc().orderByUpdateOnAsc();
                } else {
                    selector.orderBy(Memo_Schema.expiredOn.getEscapedName() + " IS NULL ASC").orderByExpiredOnAsc().orderByUpdateOnDesc();
                }
                break;
        }

        selector.orderByIdDesc();

        switch (searchMode) {
            case NONE:
                selector.doneNotEq(true);
                break;
            case NOTE:
                if (TextUtils.isEmpty(searchWord)) {
                    return memos;
                }
                selector.doneNotEq(true).where(Memo_Schema.note.getEscapedName() + " LIKE ? ESCAPE '$'", "%" + searchWord.replace("$", "$$").replace("%", "$%") + "%");
                break;
            case TAG:
                if (TextUtils.isEmpty(searchWord)) {
                    return memos;
                }
                List<Tag> tags = Tag.selectTagBySearchWord(orma, searchWord);
                if (tags.size() > 0) {
                    List<Long> memoIds = new ArrayList<>();
                    MemoTag_Selector subSelector = orma.selectFromMemoTag();
                    List<Object> tagIds = new ArrayList<>();
                    StringBuilder where = new StringBuilder();
                    String delimiter = "";
                    where.append(MemoTag_Schema.tag.getEscapedName());
                    where.append(" IN ( ");
                    for (Tag tag : tags) {
                        where.append(delimiter);
                        where.append("?");
                        delimiter = " , ";

                        tagIds.add(tag.getId());
                    }
                    where.append(" )");
                    subSelector.where(where.toString(), tagIds.toArray(new Object[tagIds.size()]));
                    for (MemoTag memoTag : subSelector.toList()) {
                        memoIds.add(memoTag.getMemo().getId());
                    }
                    selector.doneNotEq(true).idIn(memoIds.toArray(new Long[memoIds.size()]));
                } else {
                    return memos;
                }
                break;
            case TRASH:
                selector.doneEq(true);
                break;
        }

        for (Memo memo : selector.toList()) {
            memo.setTags(orma);
            memos.add(memo);
        }

        return memos;
    }

    public static Memo getMemo(OrmaDatabase orma, long id) {
        Memo memo = orma.selectFromMemo().idEq(id).valueOrNull();
        if (memo != null) {
            memo.setTags(orma);
        }

        return memo;
    }

    public static Memo create(OrmaDatabase orma, final Memo memo) {
        return orma.createMemo(new ModelFactory<Memo>() {
            @NonNull
            @Override
            public Memo call() {
                return memo;
            }
        });
    }

    public static int update(OrmaDatabase orma, Memo memo) {
        return orma.updateMemo()
                .note(memo.getNote())
                .title(memo.getTitle())
                .expiredOn(memo.getExpiredOn())
                .priority(memo.getPriority())
                .updateOn(memo.getUpdateOn())
                .idEq(memo.getId())
                .execute();
    }

    public static int updateDone(OrmaDatabase orma, long id, boolean done) {
        return orma.updateMemo()
                .done(done)
                .idEq(id)
                .execute();
    }

    public static int deleteCompletely(OrmaDatabase orma) {
        return orma.deleteFromMemo().doneEq(true).execute();
    }
}
