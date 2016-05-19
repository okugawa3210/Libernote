package com.okugawa3210.libernote.di;

import com.okugawa3210.libernote.di.scope.FragmentScope;
import com.okugawa3210.libernote.fragment.MemoFragment;
import com.okugawa3210.libernote.fragment.TagAddDialogFragment;
import com.okugawa3210.libernote.fragment.TagListFragment;
import com.okugawa3210.libernote.fragment.TagSelectDialogFragment;
import com.okugawa3210.libernote.fragment.base.MemoFormFragment;
import com.okugawa3210.libernote.fragment.base.MemoListFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {FragmentModule.class})
public interface FragmentComponent {
    void inject(MemoListFragment fragment);

    void inject(MemoFragment fragment);

    void inject(MemoFormFragment fragment);

    void inject(TagListFragment fragment);

    void inject(TagAddDialogFragment fragment);

    void inject(TagSelectDialogFragment fragment);
}
