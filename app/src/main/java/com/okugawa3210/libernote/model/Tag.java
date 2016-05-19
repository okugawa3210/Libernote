package com.okugawa3210.libernote.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.gfx.android.orma.ModelFactory;
import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import com.okugawa3210.libernote.model.base.BaseModel;

import java.util.List;

@Table
public class Tag implements BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;
    @Column(indexed = true)
    String name;

    private boolean selected;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static List<Tag> getTags(OrmaDatabase orma) {
        return orma.selectFromTag().orderByIdAsc().toList();
    }

    public static Tag getTagByName(OrmaDatabase orma, String name) {
        return orma.selectFromTag().nameEq(name).valueOrNull();
    }

    public static List<Tag> selectTagBySearchWord(OrmaDatabase orma, String searchWord) {
        Tag_Selector selector = orma.selectFromTag().orderByIdAsc();
        if (!TextUtils.isEmpty(searchWord)) {
            selector.where(" `name` LIKE ? ESCAPE '$'", "%" + searchWord.replace("$", "$$").replace("%", "$%") + "%");
        }
        return selector.toList();
    }

    public static Tag create(OrmaDatabase orma, final String tagName) {
        return orma.createTag(new ModelFactory<Tag>() {
            @NonNull
            @Override
            public Tag call() {
                Tag tag = new Tag();
                tag.setName(tagName);
                return tag;
            }
        });
    }

    public static int delete(OrmaDatabase orma, long id) {
        return orma.deleteFromTag().idEq(id).execute();
    }
}
